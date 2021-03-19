package org.nearmi.shop.security;

import org.nearmi.core.security.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
@Order(1)
@Profile("!test")
public class ShopSecurityConfig extends SecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(
                        "/v1/search*",
                        "/v1/*/image/*",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                )
                .permitAll()
                .antMatchers("/v1/pro/**").hasRole("professional")
                .anyRequest()
                .authenticated();
    }
}
