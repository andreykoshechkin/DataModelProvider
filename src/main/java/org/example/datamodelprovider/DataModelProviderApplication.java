package org.example.datamodelprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DataModelProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataModelProviderApplication.class, args);
    }

}
