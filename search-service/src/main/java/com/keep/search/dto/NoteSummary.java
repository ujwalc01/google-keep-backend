package com.keep.search.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class NoteSummary {

    private Long id;
    private String title;
    private String content;
    private Boolean pinned;
    private Boolean archived;
    private String color;
    private List<String> labels;
    private Instant reminderAt;
    private Instant createdAt;
    private Instant updatedAt;
}
