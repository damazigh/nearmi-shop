package org.nearmi.shop;

import org.nearmi.core.interceptor.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("org.nearmi")
@EnableMongoRepositories({"org.nearmi.core.repository", "org.nearmi.shop.repository"})
public class ShopApplication implements WebMvcConfigurer {
    @Autowired
    private PaginationInterceptor interceptor;
    @Value("${nearmi.cors.allowedOrigins}")
    private String[] allowedOrigins;
    @Value("${nearmi.cors.allowedMethods}")
    private String[] allowedMethods;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods(allowedMethods);
    }
}
