package org.nearmi.shop.rest.v1;

import org.nearmi.shop.dto.ConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/config")

public class ConfigController {
    @Autowired
    private Environment env;

    @GetMapping
    public ConfigDto loadConfig() {
        ConfigDto config = new ConfigDto();
        config.setMaxImageForShop(env.getRequiredProperty("nearmi.config.max-image-for-shop", Integer.class));
        config.setAcceptedImageMime(env.getRequiredProperty("nearmi.config.acceptedImageMime", String[].class));
        return config;
    }
}
