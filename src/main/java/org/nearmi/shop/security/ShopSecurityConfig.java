package org.nearmi.shop.security;

import org.nearmi.core.security.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
@Order(1)
public class ShopSecurityConfig extends SecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(
                        "/shop/v1/search*",
                        "/shop/v1/*/image"
                )
                .permitAll()
                .antMatchers("/shop/v1/pro/**").hasRole("professional")
                .anyRequest()
                .authenticated();
    }
}
