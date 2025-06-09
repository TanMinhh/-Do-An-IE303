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
public class GeminiListEntity {
    private List<GeminiEntity> models;

    public List<GeminiEntity> data() {
        return models;
    }
}
