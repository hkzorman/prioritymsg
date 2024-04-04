package com.github.hkzorman.prioritymsg.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hkzorman.prioritymsg.models.UserModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/kat98radio/**")).permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() throws IOException {
        var usersFilePath = Paths.get(System.getProperty("user.dir"), "users.json");
        System.out.println("Loading users from file: " + usersFilePath);
        if (!Files.exists(usersFilePath)) throw new IOException("Unable to find users file '" + usersFilePath + "'");

        var usersStr = Files.readString(usersFilePath);
        var users = new ObjectMapper().readValue(usersStr, UserModel[].class);
        var usersDetails = new ArrayList<UserDetails>(users.length);

        for (var user : users) {
            usersDetails.add(User.withDefaultPasswordEncoder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER")
                        .build());
        }

        return new InMemoryUserDetailsManager(usersDetails);
    }
}
