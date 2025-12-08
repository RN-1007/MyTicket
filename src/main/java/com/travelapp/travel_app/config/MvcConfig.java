package com.travelapp.travel_app.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("hotel-photos", registry);
        exposeDirectory("transport-photos", registry);
        exposeDirectory("attraction-photos", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        
        // --- PERBAIKAN DISINI ---
        // Menggunakan .toUri().toString() akan otomatis menghasilkan format yang benar:
        // Windows: file:/C:/Users/...
        // Linux:   file:///var/www/... (3 garis miring = lokal path)
        String uploadPath = uploadDir.toUri().toString(); 

        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations(uploadPath);
    }
}