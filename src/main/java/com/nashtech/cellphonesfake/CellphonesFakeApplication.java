package com.nashtech.cellphonesfake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class CellphonesFakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CellphonesFakeApplication.class, args);
    }

}
