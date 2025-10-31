package com.foodcourt.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MsUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsersApplication.class, args);
    }

}
