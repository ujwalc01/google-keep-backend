package com.keep.search.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {

    private String query;
    private int total;
    private List<NoteSummary> notes;
}
