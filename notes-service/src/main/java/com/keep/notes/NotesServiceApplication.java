package com.keep.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.keep")
public class NotesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesServiceApplication.class, args);
    }
}
