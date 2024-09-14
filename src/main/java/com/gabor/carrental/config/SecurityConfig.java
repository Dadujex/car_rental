package com.gabor.carrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Admin paths should be protected
                .anyRequest().permitAll()  // Other paths can be accessed without authentication
            )
                .formLogin(form -> form.loginPage("/admin/login").failureForwardUrl("/").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User
                    .withUsername("admin")
                    .password("{noop}password")
                    .roles("ADMIN")
                    .build();

        return new InMemoryUserDetailsManager(user);
    }

}

