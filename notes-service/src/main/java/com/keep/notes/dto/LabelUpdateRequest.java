package com.keep.notes.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelUpdateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String color;
}
