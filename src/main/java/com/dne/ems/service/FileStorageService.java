package com.dne.ems.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.dne.ems.model.Attachment;
import com.dne.ems.model.Feedback;

/**
 * Interface for file storage operations.
 */
public interface FileStorageService {

    /**
     * Stores a file and associates it with a feedback entity.
     *
     * @param file     the file to store
     * @param feedback the feedback entity to associate the file with
     * @return the created Attachment entity
     */
    Attachment storeFile(MultipartFile file, Feedback feedback);

    /**
     * Loads a file as a resource from the storage location.
     *
     * @param fileName the name of the file to load
     * @return the file as a Resource
     */
    Resource loadFileAsResource(String fileName);

}