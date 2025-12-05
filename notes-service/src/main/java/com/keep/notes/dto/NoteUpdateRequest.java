package com.keep.notes.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteUpdateRequest {

    @Size(max = 255)
    private String title;

    private String content;

    @Size(max = 50)
    private String color;

    private Boolean pinned;

    private Boolean archived;

    private Boolean deleted;
}
