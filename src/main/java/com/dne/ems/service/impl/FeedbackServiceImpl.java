package com.dne.ems.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dne.ems.dto.FeedbackSubmissionRequest;
import com.dne.ems.dto.PublicFeedbackRequest;
import com.dne.ems.event.FeedbackSubmittedForAiReviewEvent;
import com.dne.ems.exception.ResourceNotFoundException;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.repository.FeedbackRepository;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.FeedbackService;
import com.dne.ems.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserAccountRepository userAccountRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public Feedback submitFeedback(FeedbackSubmissionRequest request, MultipartFile[] files) {
        // Security Fix: Get user from the security context, not from a request parameter.
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        // Ensure the user exists before proceeding.
        userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Feedback feedback = new Feedback();
        feedback.setTitle(request.title());
        feedback.setDescription(request.description());
        feedback.setPollutionType(request.pollutionType());
        feedback.setSeverityLevel(request.severityLevel());
        
        // Set location data from the DTO. The @NotNull validation on the DTO ensures location is not null.
        feedback.setLatitude(request.location().latitude());
        feedback.setLongitude(request.location().longitude());
        feedback.setTextAddress(request.location().textAddress());
        feedback.setGridX(request.location().gridX());
        feedback.setGridY(request.location().gridY());

        feedback.setEventId(UUID.randomUUID().toString());
        feedback.setStatus(FeedbackStatus.AI_REVIEWING); // Correct initial status
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());
        feedback.setSubmitterId(userId); // Assuming Feedback has setSubmitterId

        // First, save the feedback entity to generate an ID
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Now, store the files and associate them with the saved feedback
        if (files != null && files.length > 0) {
            Arrays.stream(files)
                  .filter(file -> file != null && !file.isEmpty())
                  .forEach(file -> fileStorageService.storeFile(file, savedFeedback));
        }
        
        // Publish an event to trigger AI review
        eventPublisher.publishEvent(new FeedbackSubmittedForAiReviewEvent(this, savedFeedback));
        
        return savedFeedback;
    }

    @Override
    public Page<Feedback> getAllFeedback(Pageable pageable) {
        return feedbackRepository.findAll(pageable);
    }
    @Override
    @Transactional
    public Feedback createPublicFeedback(PublicFeedbackRequest request, MultipartFile[] files) {
        Feedback feedback = new Feedback();
        feedback.setTitle(request.getTitle());
        feedback.setDescription(request.getDescription());
        try {
            feedback.setPollutionType(PollutionType.valueOf(request.getPollutionType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            // In a real app, you might throw a specific exception.
            // For now, we can ignore or set a default
        }
        
        // Assuming PublicFeedbackRequest also has lat/lon
        // If not, this part needs adjustment based on DTO definition
        // For now, let's assume it might be null or needs a different DTO.
        // We will leave this part for when we refactor PublicFeedbackRequest.
        // feedback.setLatitude(request.getLatitude());
        // feedback.setLongitude(request.getLongitude());

        feedback.setEventId(UUID.randomUUID().toString());
        feedback.setStatus(FeedbackStatus.AI_REVIEWING); // Correct initial status
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());
        // Submitter is anonymous, so submitterId remains null

        // First, save the feedback entity to generate an ID
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Now, store the files and associate them with the saved feedback
        if (files != null && files.length > 0) {
            Arrays.stream(files)
                  .filter(file -> file != null && !file.isEmpty())
                  .forEach(file -> fileStorageService.storeFile(file, savedFeedback));
        }

        // Publish an event to trigger AI review
        eventPublisher.publishEvent(new FeedbackSubmittedForAiReviewEvent(this, savedFeedback));

        return savedFeedback;
    }
}