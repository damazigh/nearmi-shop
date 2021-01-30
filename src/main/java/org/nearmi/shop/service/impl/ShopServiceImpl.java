package org.nearmi.shop.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.exception.MiException;
import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.Address;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.mongo.document.shopping.ShopOptions;
import org.nearmi.core.repository.AddressRepository;
import org.nearmi.core.repository.CoreUserRepository;
import org.nearmi.core.resource.GeneralResKey;
import org.nearmi.core.security.CoreSecurity;
import org.nearmi.core.service.impl.UploadService;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.in.SearchShopDto;
import org.nearmi.shop.repository.ShopOptionsRepository;
import org.nearmi.shop.repository.ShopRepository;
import org.nearmi.shop.rest.ShopResKey;
import org.nearmi.shop.service.IShopService;
import org.nearmi.shop.validator.ShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

import static org.nearmi.core.validator.Validator.*;
import static org.nearmi.shop.validator.ShopValidator.validateClosureTimeCoherence;

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
        Point point = new Point(shopDto.getAddress().getLongitude(), shopDto.getAddress().getLatitude());
        address.setLocation(point);
        shop.setAddress(address);
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
    public void updateImage(MultipartFile file, String shopId) {
        notEmpty(file, "image");
        MiProUser pro = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        validateProUser(pro);
        Shop targetShop = ShopValidator.validateShopBelongToUser(pro, shopId);
        if (StringUtils.isNotBlank(targetShop.getImageMetadata())) {
            uploadService.deleteIfExist(targetShop.getImageMetadata());
        }
        String image = uploadService.upload(file, pro.getId(), shopId, env.getRequiredProperty("nearmi.config.acceptedImageMime", String[].class));
        targetShop.setImageMetadata(image);
        shopRepository.save(targetShop);
    }

    @Override
    public byte[] loadImage(String shopId) {
        Optional<Shop> opShop = shopRepository.findById(shopId);
        if (opShop.isPresent()) {
            Shop shop = opShop.get();
            if (shop.getImageMetadata() != null) {
                return uploadService.load(shop.getImageMetadata());
            }
        }
        return null;
    }

    @Override
    public Collection<Shop> getBelongingShop() {
        MiProUser proUser = proUserRepo.findByUsername(CoreSecurity.token().getPreferredUsername());
        if (proUser != null) {
            return proUser.getShops();
        }
        throw new MiException(GeneralResKey.NMI_G_0001);
    }

    private void isValidRegistrationNumber(String registrationNumber) {
        // TODO implémenter la validation du siret
    }

    private void validateProUser(MiProUser proUser) {
        if (proUser == null) { // TODO modifier la clé
            throw new MiException(ShopResKey.NMI_S_0001);
        }
    }
}
