package com.fitconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FitConnect - Main Application Entry Point
 * LinkedIn-style platform for Fitness Professionals
 */
@SpringBootApplication
public class FitConnectApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitConnectApplication.class, args);
        System.out.println("🏋️ FitConnect Backend Started Successfully!");
        System.out.println("📡 API running at: http://localhost:8080/api");
    }
}
