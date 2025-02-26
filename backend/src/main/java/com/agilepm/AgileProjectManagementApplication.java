package com.agilepm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.agilepm.model")
@EnableJpaRepositories(basePackages = "com.agilepm.repository")
public class AgileProjectManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileProjectManagementApplication.class, args);
    }
}
