package de.muenchen.referenzarchitektur.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author rowe42
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.setMaxAge(3600L);
        config.addAllowedHeader("*");
//        config.addAllowedHeader("Origin");
//        config.addAllowedHeader("X-Requested-With");
//        config.addAllowedHeader("Content-Type");
//        config.addAllowedHeader("Accept");
//        config.addAllowedHeader("Authorization");
//        config.addAllowedHeader("x-auth-token");
//        config.addAllowedHeader("x-requested-with");
        config.addExposedHeader("Location");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
