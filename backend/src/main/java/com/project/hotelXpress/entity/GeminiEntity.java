package com.project.hotelXpress.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiEntity {
    private String name;
    private String version;
    private String displayName;
    private String description;
    private int inputTokenLimit;
    private int outputTokenLimit;
    private List<String> supportedGenerationMethods;
}
