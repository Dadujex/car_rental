package com.gabor.carrental;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan("com.gabor")
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }

    @PostConstruct
    public void init() {
        File directory = new File("src/main/resources/static/images");
        if(!directory.exists()){
            directory.mkdirs();
        }
    }
}
