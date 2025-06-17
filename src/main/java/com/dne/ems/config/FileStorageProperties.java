package com.dne.ems.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {

    /**
     * The directory where uploaded files will be stored.
     * This path should be configured in the application.yml file.
     * Example: file.upload-dir=/path/to/your/upload-dir
     */
    private String uploadDir;

}