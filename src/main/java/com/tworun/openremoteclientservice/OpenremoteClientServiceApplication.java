package com.tworun.openremoteclientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OpenremoteClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenremoteClientServiceApplication.class, args);
    }

}
