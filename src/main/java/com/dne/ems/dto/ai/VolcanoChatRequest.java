package com.dne.ems.dto.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolcanoChatRequest {
    private String model;
    private List<Message> messages;
    private List<Tool> tools;

    @Data
    @SuperBuilder
    public static class Message {
        private String role;
        private List<ContentPart> content;
    }
    
    public interface ContentPart {
    }

    @Data
    @SuperBuilder
    public static class TextContentPart implements ContentPart {
        @Builder.Default
        private String type = "text";
        private String text;
    }

    @Data
    @SuperBuilder
    public static class ImageUrlContentPart implements ContentPart {
        @Builder.Default
        private String type = "image_url";
        @JsonProperty("image_url")
        private ImageUrl imageUrl;
    }

    @Data
    @Builder
    public static class ImageUrl {
        private String url;
    }

    @Data
    @Builder
    public static class Tool {
        private String type;
        private FunctionDef function;
    }

    @Data
    @Builder
    public static class FunctionDef {
        private String name;
        private String description;
        private Parameters parameters;
    }

    @Data
    @Builder
    public static class Parameters {
        private String type;
        private Object properties;
        @JsonProperty("required")
        private List<String> required;
    }
} 