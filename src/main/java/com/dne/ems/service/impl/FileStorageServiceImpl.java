package com.dne.ems.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dne.ems.exception.FileStorageException;
import com.dne.ems.model.Attachment;
import com.dne.ems.model.Feedback;
import com.dne.ems.repository.AttachmentRepository;
import com.dne.ems.service.FileStorageService;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    private final AttachmentRepository attachmentRepository;

    // Whitelist of allowed file extensions and content types for security
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".pdf", ".doc", ".docx");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/png", "image/jpeg", "image/gif", "application/pdf", 
            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir, AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public Attachment storeFile(MultipartFile file, Feedback feedback) {
        if (this.fileStorageLocation == null) {
            throw new FileStorageException("File storage location is not configured.");
        }
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Cannot store an empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new FileStorageException("File name is invalid or not provided.");
        }
        String cleanedFilename = StringUtils.cleanPath(originalFilename);

        // Security Check: Validate filename
        if (cleanedFilename.contains("..")) {
            throw new FileStorageException("Cannot store file with relative path outside current directory " + cleanedFilename);
        }

        // Security Check: Validate file extension and content type against a whitelist
        String fileExtension = getFileExtension(cleanedFilename);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new FileStorageException("Invalid file extension. Only " + ALLOWED_EXTENSIONS + " are allowed.");
        }
        
        String contentType = file.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new FileStorageException("Invalid file content type. Only " + ALLOWED_CONTENT_TYPES + " are allowed.");
        }

        try {
            // Generate a unique file name to avoid collisions
            String storedFilename = UUID.randomUUID().toString() + fileExtension;

            Path targetLocation = this.fileStorageLocation.resolve(storedFilename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            Attachment attachment = Attachment.builder()
                    .fileName(cleanedFilename)
                    .storedFileName(storedFilename)
                    .fileType(contentType)
                    .fileSize(file.getSize())
                    .feedback(feedback)
                    .build();

            return attachmentRepository.save(attachment);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + cleanedFilename + ". Please try again!", ex);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("File not found or not readable: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found: " + fileName, ex);
        }
    }
}