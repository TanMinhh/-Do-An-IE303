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
public class GeminiList {
    private List<Gemini> models;

    public List<Gemini> data() {
        return models;
    }
}