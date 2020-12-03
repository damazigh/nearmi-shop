package org.nearmi.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("org.nearmi")
@EnableMongoRepositories
public class ShopApplication {

    public static void main(String [] args) {
        SpringApplication.run(ShopApplication.class);
    }
}
