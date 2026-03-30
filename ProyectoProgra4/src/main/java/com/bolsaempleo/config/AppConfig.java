package com.bolsaempleo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${app.cv.upload-dir}")
    private String cvUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        new File(cvUploadDir).mkdirs();

        registry.addResourceHandler("/uploads/cv/**")
                .addResourceLocations("file:" + cvUploadDir + "/");
    }
}
