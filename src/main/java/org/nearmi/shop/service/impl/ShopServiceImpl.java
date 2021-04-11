package org.nearmi.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.exception.MiException;
import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.Address;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.mongo.document.shopping.ShopOptions;
import org.nearmi.core.mongo.document.technical.ImageMetadata;
import org.nearmi.core.repository.AddressRepository;
import org.nearmi.core.repository.CoreUserRepository;
import org.nearmi.core.resource.GeneralResKey;
import org.nearmi.core.security.CoreSecurity;
import org.nearmi.core.service.impl.UploadService;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.in.ImageBoundariesDto;
import org.nearmi.shop.dto.in.SearchShopDto;
import org.nearmi.shop.repository.ShopOptionsRepository;
import org.nearmi.shop.repository.ShopRepository;
import org.nearmi.shop.rest.ShopResKey;
import org.nearmi.shop.service.IShopService;
import org.nearmi.shop.validator.ShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.nearmi.core.validator.Validator.*;
import static org.nearmi.shop.validator.ShopValidator.validateClosureTimeCoherence;

@Slf4j
@Service
public class ShopServiceImpl implements IShopService {
    @Autowired
    private CoreUserRepository<MiProUser> proUserRepo;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopOptionsRepository shopOptionsRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private Environment env;


    @Override
    public void create(ShopDto shopDto) {

        MiProUser proUser = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        notNull(shopDto, "shop");
        notEmpty(shopDto.getRegistrationNumber(), "shop registration number");
        isValidRegistrationNumber(shopDto.getRegistrationNumber());
        notEmpty(shopDto.getName(), "shop name");
        notNull(shopDto.getOpensAt(), "shop opens at");
        notNull(shopDto.getClosesAt(), "shop closes at");
        notNull(shopDto.getAddress(), "shop address");
        validateClosureTimeCoherence(shopDto.getOpensAt(), shopDto.getClosesAt(), ShopResKey.NMI_S_0001);

        if (!shopDto.isWithoutBreakClosure()) {
            notNull(shopDto.getBreakClosureStart(), "shop break closure start");
            notNull(shopDto.getBreakClosureEnd(), "shop break closure end");
            validateClosureTimeCoherence(shopDto.getBreakClosureStart(), shopDto.getBreakClosureEnd(), ShopResKey.NMI_S_0001);
        }
        validateAddress(shopDto.getAddress());

        Shop shop = new Shop();
        shop.setName(shopDto.getName());
        shop.setDescription(shopDto.getDescription());
        shop.setShortDesc(shopDto.getShortDescription());
        shop.setRegistrationNumber(shopDto.getRegistrationNumber());

        // shop options
        ShopOptions options = new ShopOptions();
        options.setAutomaticOrderConfirmation(shopDto.isAutomaticOrderConfirmation());
        options.setOpensAt(shopDto.getOpensAt());
        options.setClosesAt(shopDto.getClosesAt());
        options.setManualOrderConfirmation(!shopDto.isAutomaticOrderConfirmation());
        options.setOpenWithoutClosure(shopDto.isWithoutBreakClosure());
        if (!shopDto.isWithoutBreakClosure()) {
            options.setBreakClosureStart(shopDto.getBreakClosureStart());
            options.setBreakClosureEnd(shopDto.getBreakClosureEnd());
        }
        shop.setResponsible(proUser);
        options.setSchedulingAppointment(shopDto.isSchedulingAppointment());
        shop.setOptions(options);

        // address
        Address address = new Address();
        address.setCity(shopDto.getAddress().getCity());
        address.setCountry(shopDto.getAddress().getCountry());
        address.setPostalCode(shopDto.getAddress().getPostalCode());
        address.setLine1(shopDto.getAddress().getLine1());
        address.setLine2(shopDto.getAddress().getLine2());
        GeoJsonPoint point = new GeoJsonPoint(shopDto.getAddress().getLongitude(), shopDto.getAddress().getLatitude());
        address.setLocation(point);
        shop.setAddress(address);
        addressRepository.ensureGeo2dIndex();
        shopRepository.save(shop);
    }

    @Override
    public PaginatedSearchResult<Shop> search(SearchShopDto searchShopDto, Pageable pageable) {
        notNull(searchShopDto.getAddress(), "address");
        Collection<Address> addresses = addressRepository.findByLocation(searchShopDto.getAddress().getLongitude(), searchShopDto.getAddress().getLatitude());
        if (addresses != null && !addresses.isEmpty()) {
            return PaginatedSearchResult.of(shopRepository.findByAddressesIds(addresses, pageable));
        }
        return PaginatedSearchResult.of(null);
    }

    @Override
    public void updateImages(MultipartFile[] files, String shopId, String rootImage, List<ImageBoundariesDto> boundaries) {
        // prerequisites validation
        notEmpty(files, "uploaded files");
        notEmpty(boundaries, "image boundaries");
        equal(files.length, boundaries.size(), "NMI_S_0004");

        // business validation
        MiProUser pro = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop targetShop = ShopValidator.validateShopBelongToUser(pro, shopId);
        int actualSize = targetShop.getMetadata().size();
        log.debug("Already saved {} image for shop with id : {}", actualSize, shopId);
        int afterUploadSize = actualSize + files.length;
        int maxAuthorizedFile = env.getRequiredProperty("nearmi.config.max-image-for-shop", Integer.class);

        if (afterUploadSize > maxAuthorizedFile) {
            log.error("image limit exceeded ({}), trying to save {} images", maxAuthorizedFile, afterUploadSize);
            throw new MiException(ShopResKey.NMI_S_0002, String.valueOf(maxAuthorizedFile), String.valueOf(afterUploadSize));
        }
        int i = 0;
        for (MultipartFile file : files) {
            notEmpty(file, "image");
            ImageBoundariesDto imgBoundaries = boundaries.get(i);
            notEmpty(imgBoundaries, "image boundaries");
            log.debug("saving image  {} for shop {}", file.getOriginalFilename(), shopId);
            String path = uploadService.upload(file, pro.getId(), shopId, env.getRequiredProperty("nearmi.config.acceptedImageMime", String[].class));
            log.debug("image {} saved at {}", file.getOriginalFilename(), path);
            ImageMetadata metadata = new ImageMetadata(pro.getId(), isRootImage(rootImage, path), path, imgBoundaries.getWidth(), imgBoundaries.getHeight());
            log.debug("created metadata referencing image at path {} with boundaries [h: {}, w: {}]", path, imgBoundaries.getHeight(), imgBoundaries.getWidth());
            targetShop.getMetadata().add(metadata);
            log.debug("target shop {} metadata updated", targetShop.getId());
            i++;
        }
        // user didn't chose root image, set first image as root
        if (!targetShop.getMetadata().isEmpty() && targetShop.getMetadata().stream().noneMatch(ImageMetadata::isRootImage)) {
            ImageMetadata metadata = targetShop.getMetadata().get(0);
            metadata.setRootImage(true);
            log.warn("user didn't provide a root image. Image saved at path {} marked as root", metadata.getPath());

        }
        shopRepository.save(targetShop);
    }

    @Override
    public byte[] loadImage(String shopId, String name) {
        Optional<Shop> opShop = shopRepository.findById(shopId);
        if (opShop.isPresent()) {
            Shop shop = opShop.get();
            ImageMetadata imageMetadata = findImageMetadataByName(shop.getMetadata(), name);
            if (imageMetadata != null) {
                return uploadService.load(imageMetadata.getPath());
            }
        }
        throw new MiException(GeneralResKey.NMI_G_0010); // 404 not found
    }

    @Override
    public Collection<Shop> getBelongingShop() {
        MiProUser proUser = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        if (proUser != null) {
            return proUser.getShops();
        }
        throw new MiException(GeneralResKey.NMI_G_0001);
    }

    @Override
    public Shop getDetail(String shopId) {
        MiProUser proUser = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        return ShopValidator.validateShopBelongToUser(proUser, shopId);
    }

    @Override
    public void delete(String[] images, String shopId) {
        log.info("requesting deletion images {} - for shop {}", images, shopId);
        MiProUser pro = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop targetShop = ShopValidator.validateShopBelongToUser(pro, shopId);
        List<String> metaNameToRemove = new ArrayList<>();
        targetShop.getMetadata().forEach(m -> {
            if (Arrays.stream(images).anyMatch(img -> StringUtils.equals(m.getName(), img))) {
                log.debug("trying to delete metadata with name {}", m.getName());
                uploadService.deleteIfExist(m.getPath());
                log.debug("image at path {} deleted (if it exists)", m.getPath());
                metaNameToRemove.add(m.getName());
                log.debug("added {} to tracked list of metadata to remove from shop with id {}", m.getName(), shopId);
            }
        });
        boolean res = targetShop.getMetadata().removeIf(
                m -> metaNameToRemove.stream()
                        .anyMatch(name -> StringUtils.equals(m.getName(), name)));
        if (res) {
            shopRepository.save(targetShop);
            log.debug("target shop with id {} updated", shopId);
        }

    }

    @Override
    public void markAsRoot(String shopId, String name) {
        notEmpty(name, "image name");
        MiProUser pro = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop targetShop = ShopValidator.validateShopBelongToUser(pro, shopId);
        Optional<ImageMetadata> optional = targetShop.getMetadata().stream().filter(m -> m.getName().equals(name))
                .findFirst();
        if (optional.isEmpty()) {
            throw new MiException(ShopResKey.NMI_S_0002);
        }
        Optional<ImageMetadata> oldOp = targetShop.getMetadata().stream().filter(ImageMetadata::isRootImage).findFirst();
        ImageMetadata old = null;
        if (oldOp.isPresent()) {
            old = oldOp.get();
            old.setRootImage(false);
        }
        ImageMetadata toUpdate = optional.get();
        toUpdate.setRootImage(true);
        shopRepository.save(targetShop);
    }

    private void isValidRegistrationNumber(String registrationNumber) {
        // TODO implémenter la validation du siret
    }

    private ImageMetadata findImageMetadataByName(List<ImageMetadata> metadata, String name) {
        if (metadata != null && !metadata.isEmpty()) {
            return metadata.stream().filter(m -> StringUtils.equals(FilenameUtils.getName(m.getPath()), name)).findFirst().orElse(null);
        }
        return null;
    }

    private boolean isRootImage(String rootImage, String path) {
        return StringUtils.equals(FilenameUtils.getName(path), rootImage);
    }

}
