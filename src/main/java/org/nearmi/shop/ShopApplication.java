package org.nearmi.shop;

import org.nearmi.core.interceptor.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("org.nearmi")
@EnableMongoRepositories({"org.nearmi.core.repository", "org.nearmi.shop.repository"})
public class ShopApplication implements WebMvcConfigurer {
    @Autowired
    private PaginationInterceptor interceptor;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
