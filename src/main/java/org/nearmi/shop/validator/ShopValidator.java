package org.nearmi.shop.validator;


import org.nearmi.core.exception.MiException;
import org.nearmi.core.mongo.document.technical.Time;

public class ShopValidator {
    public static void validateClosureTimeCoherence(Time open, Time close, String key) {
        if (open.getHour() < open.getHour()) {
            throw new MiException(key, open.toString(), close.toString());
        }
    }
}
