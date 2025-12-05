package com.keep.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LabelResponse {

    private Long id;
    private String name;
    private String color;
}
