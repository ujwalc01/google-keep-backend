package com.keep.notes.dto;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoteResponse {

    private Long id;
    private String title;
    private String content;
    private String color;
    private boolean pinned;
    private boolean archived;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;
    private List<LabelResponse> labels;
    private List<Long> collaborators;
}
