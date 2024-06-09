package com.example.gestiontask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class GestionTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTaskApplication.class, args);
    }

}
