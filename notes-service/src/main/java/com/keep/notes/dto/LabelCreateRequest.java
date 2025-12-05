package com.keep.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String color;
}

