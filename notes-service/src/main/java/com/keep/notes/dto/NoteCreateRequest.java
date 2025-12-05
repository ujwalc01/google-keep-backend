package com.keep.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteCreateRequest {

    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @Size(max = 50)
    private String color;

    private Boolean pinned;
}
