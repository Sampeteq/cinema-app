package com.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringMain.class, args);
    }
}
