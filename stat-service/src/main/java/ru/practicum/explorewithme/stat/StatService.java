package ru.practicum.explorewithme.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.explorewithme")
public class StatService {
    public static void main(String[] args) {
        SpringApplication.run(StatService.class, args);
    }
}