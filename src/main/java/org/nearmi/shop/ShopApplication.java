package org.nearmi.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("org.nearmi")
@EnableMongoRepositories({"org.nearmi.core.repository", "org.nearmi.shop.repository"})
public class ShopApplication {

    public static void main(String [] args) {
        SpringApplication.run(ShopApplication.class);
    }
}
