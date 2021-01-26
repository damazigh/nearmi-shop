package org.nearmi.shop.validator;


import org.nearmi.core.exception.MiException;
import org.nearmi.core.exception.UnauthorizedException;
import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.mongo.document.technical.Time;
import org.nearmi.core.validator.Validator;

public class ShopValidator {
    public static void validateClosureTimeCoherence(Time open, Time close, String key) {
        if (open.getHour() < open.getHour()) {
            throw new MiException(key, open.toString(), close.toString());
        }
    }

    public static Shop validateShopBelongToUser(MiProUser proUser, String shopId) {
        Validator.notNull(proUser, "user");
        return proUser.getShops().stream()
                .filter(s -> s.getId().equalsIgnoreCase(shopId))
                .findFirst().orElseThrow(() -> new UnauthorizedException(String.format("Unauthorized ! not allowed to update shop with id %1s", shopId)));
    }
}
