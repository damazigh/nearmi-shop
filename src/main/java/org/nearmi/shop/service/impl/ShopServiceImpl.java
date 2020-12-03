package org.nearmi.shop.service.impl;

import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.MiUser;
import org.nearmi.core.mongo.document.shopping.Address;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.mongo.document.shopping.ShopOptions;
import org.nearmi.core.repository.AddressRepository;
import org.nearmi.core.repository.CoreUserRepository;
import org.nearmi.core.security.CoreSecurity;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.repository.ShopOptionsRepository;
import org.nearmi.shop.repository.ShopRepository;
import org.nearmi.shop.rest.ShopResKey;
import org.nearmi.shop.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.nearmi.core.validator.Validator.notEmpty;
import static org.nearmi.core.validator.Validator.notNull;
import static org.nearmi.core.validator.Validator.validateAddress;
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

    public void create(ShopDto shopDto) {
        notNull(shopDto, "shop");
        notEmpty(shopDto.getRegistrationNumber(), "shop registration umber");
        isValidRegistrationNumber(shopDto.getRegistrationNumber());
        notEmpty(shopDto.getName(),"shop name");
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
        MiUser proUser = proUserRepo.findByUsername(CoreSecurity.getAttribute("username"));

        Shop shop = new Shop();
        shop.setName(shopDto.getName());
        shop.setDescription(shopDto.getDescription());
        shop.setShortDesc(shopDto.getShortDescription());

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
        options.setSchedulingAppointment(shopDto.isSchedulingAppointment());
        shopOptionsRepository.save(options);
        shop.setOptions(options);

        // address
        Address address = new Address();
        address.setCity(shopDto.getAddress().getCity());
        address.setCountry(shopDto.getAddress().getCountry());
        address.setPostalCode(shopDto.getAddress().getPostalCode());
        address.setLine1(shopDto.getAddress().getLine1());
        address.setLine2(shopDto.getAddress().getLine2());
        address.setLongitude(shopDto.getAddress().getLongitude());
        address.setLatitude(shopDto.getAddress().getLatitude());
        addressRepository.save(address);
        shop.setAddress(address);
        shopRepository.save(shop);
    }

    private void isValidRegistrationNumber(String registrationNumber) {
        // TODO implémenter la validation du siret
    }
}
