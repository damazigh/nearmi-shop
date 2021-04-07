package org.nearmi.shop.validator;


import lombok.extern.slf4j.Slf4j;
import org.nearmi.core.exception.MiException;
import org.nearmi.core.exception.UnauthorizedException;
import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.mongo.document.technical.Time;
import org.nearmi.core.validator.Validator;

@Slf4j
public class ShopValidator {
    public static void validateClosureTimeCoherence(Time open, Time close, String key) {
        if (open.getHour() < open.getHour()) {
            throw new MiException(key, open.toString(), close.toString());
        }
    }

    public static Shop validateShopBelongToUser(MiProUser proUser, String shopId) {
        Validator.notNull(proUser, "user");
        log.debug("validating shop with id {} belongs to user with id {}", proUser.getId(), shopId);
        return proUser.getShops().stream()
                .filter(s -> s.getId().equalsIgnoreCase(shopId))
                .findFirst().orElseThrow(() -> new UnauthorizedException(String.format("Unauthorized ! not allowed to interact with shop %1s", shopId)));
    }
}
