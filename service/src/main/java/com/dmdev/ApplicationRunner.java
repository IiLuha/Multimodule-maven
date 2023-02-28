package com.dmdev;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ApplicationRunner {

    public static void main(String[] args) {
        var run = SpringApplication.run(ApplicationRunner.class, args);
        System.out.println();
    }
}
