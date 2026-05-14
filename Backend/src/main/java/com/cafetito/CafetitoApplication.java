package com.cafetito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CafetitoApplication {

    private final Environment env;

    public CafetitoApplication(Environment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logDatasource() {
        String url = env.getProperty("spring.datasource.url", "no config");
        System.out.println("[Cafetito] Conectando a BD: " + url);
    }

    public static void main(String[] args) {
        SpringApplication.run(CafetitoApplication.class, args);
    }
}
