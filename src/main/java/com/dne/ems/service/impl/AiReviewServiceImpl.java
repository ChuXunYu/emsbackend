package com.dne.ems.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dne.ems.dto.ai.VolcanoChatRequest;
import com.dne.ems.dto.ai.VolcanoChatResponse;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.repository.FeedbackRepository;
import com.dne.ems.service.AiReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiReviewServiceImpl implements AiReviewService {

    private final FeedbackRepository feedbackRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper; // For parsing JSON strings

    @Value("${volcano.api.url}")
    private String apiUrl;

    @Value("${volcano.api.key}")
    private String apiKey;

    @Value("${volcano.model.name}")
    private String modelName;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public void reviewFeedback(Feedback feedback) {
        log.info("Starting AI review for feedback ID: {}", feedback.getId());

        List<String> imageUrls = feedback.getAttachments().stream()
                .map(attachment -> appBaseUrl + "/api/files/view/" + attachment.getStoredFileName())
                .toList();

        VolcanoChatRequest request = buildRequest(feedback.getDescription(), imageUrls);

        webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .body(Mono.just(request), VolcanoChatRequest.class)
                .retrieve()
                .bodyToMono(VolcanoChatResponse.class)
                .doOnSuccess(response -> processResponse(response, feedback))
                .onErrorResume(error -> {
                    log.error("AI API call failed for feedback ID: {}. Setting status to AI_REVIEW_FAILED.", feedback.getId(), error);
                    feedback.setStatus(FeedbackStatus.AI_REVIEW_FAILED);
                    feedbackRepository.save(feedback);
                    return Mono.empty(); // Terminate the chain gracefully
                })
                .subscribe(); // Subscribe to trigger the execution
    }
    
    private VolcanoChatRequest buildRequest(String feedbackDescription, List<String> imageUrls) {
        VolcanoChatRequest.Message systemMessage = VolcanoChatRequest.Message.builder()
                .role("system")
                .content(List.of(
                        VolcanoChatRequest.TextContentPart.builder().text("你是一个内容审核员。你的任务是判断用户反馈是否合规（不含敏感词）并且是否与大气污染相关。反馈可能包含文本描述和图片，图片是可选的辅助信息。你必须主要根据文本内容进行判断，即使没有图片也要完成分析。你必须使用提供的'feedback_analysis'工具来返回你的分析结果。").build()
                ))
                .build();
        
        List<VolcanoChatRequest.ContentPart> userContentParts = new java.util.ArrayList<>();
        userContentParts.add(VolcanoChatRequest.TextContentPart.builder().text(feedbackDescription).build());
        
        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {
                userContentParts.add(VolcanoChatRequest.ImageUrlContentPart.builder()
                    .imageUrl(VolcanoChatRequest.ImageUrl.builder().url(imageUrl).build())
                    .build());
            }
        }

        VolcanoChatRequest.Message userMessage = VolcanoChatRequest.Message.builder()
                .role("user")
                .content(userContentParts)
                .build();

        VolcanoChatRequest.FunctionDef functionDef = VolcanoChatRequest.FunctionDef.builder()
                .name("feedback_analysis")
                .description("分析反馈内容并返回审核结果")
                .parameters(VolcanoChatRequest.Parameters.builder()
                        .type("object")
                        .properties(Map.of(
                                "is_compliant", Map.of("type", "boolean", "description", "内容是否合规"),
                                "is_relevant", Map.of("type", "boolean", "description", "内容是否与大气污染相关")
                        ))
                        .required(List.of("is_compliant", "is_relevant"))
                        .build())
                .build();
        
        VolcanoChatRequest.Tool tool = VolcanoChatRequest.Tool.builder()
                .type("function")
                .function(functionDef)
                .build();

        return VolcanoChatRequest.builder()
                .model(modelName)
                .messages(List.of(systemMessage, userMessage))
                .tools(List.of(tool))
                .build();
    }

    private void processResponse(VolcanoChatResponse response, Feedback feedback) {
        try {
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                VolcanoChatResponse.ToolCall toolCall = response.getChoices().get(0).getMessage().getToolCalls().get(0);
                if ("feedback_analysis".equals(toolCall.getFunction().getName())) {
                    String argumentsJson = toolCall.getFunction().getArguments();
                    Map<String, Boolean> args = objectMapper.readValue(argumentsJson, new TypeReference<Map<String, Boolean>>() {});
                    
                    boolean isCompliant = args.getOrDefault("is_compliant", false);
                    boolean isRelevant = args.getOrDefault("is_relevant", false);

                    if (isCompliant && isRelevant) {
                        feedback.setStatus(FeedbackStatus.PENDING_REVIEW);
                        log.info("AI review PASSED for feedback ID: {}. Status set to PENDING_REVIEW.", feedback.getId());
                    } else {
                        feedback.setStatus(FeedbackStatus.CLOSED_INVALID);
                        log.warn("AI review FAILED for feedback ID: {}. Compliant: {}, Relevant: {}. Status set to CLOSED_INVALID.", feedback.getId(), isCompliant, isRelevant);
                    }
                }
            } else {
                log.error("Received empty or invalid response from AI API for feedback ID: {}", feedback.getId());
                feedback.setStatus(FeedbackStatus.AI_REVIEW_FAILED);
            }
        } catch (JsonProcessingException | NullPointerException | IndexOutOfBoundsException e) {
            log.error("Failed to parse or process AI response for feedback ID: {}. Setting status to AI_REVIEW_FAILED.", feedback.getId(), e);
            feedback.setStatus(FeedbackStatus.AI_REVIEW_FAILED);
        } finally {
            feedbackRepository.save(feedback);
        }
    }
} 