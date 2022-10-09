package ru.practicum.explorewithme.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.explorewithme")
public class MainService {

    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}