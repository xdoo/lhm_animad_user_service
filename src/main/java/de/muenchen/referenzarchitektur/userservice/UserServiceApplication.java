package de.muenchen.referenzarchitektur.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author rowe42
 */
@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@ComponentScan("de.muenchen.referenzarchitektur.authorisationLib, de.muenchen.referenzarchitektur.userservice")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
