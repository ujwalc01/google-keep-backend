package com.keep.search.controller;

import com.keep.search.dto.SearchResult;
import com.keep.search.service.NoteSearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class NoteSearchController {

    private final NoteSearchService noteSearchService;

    public NoteSearchController(NoteSearchService noteSearchService) {
        this.noteSearchService = noteSearchService;
    }

    @GetMapping("/notes")
    public ResponseEntity<SearchResult> searchNotes(@RequestParam(name = "query", required = false) String query,
                                                    HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String cacheKey = authHeader != null ? authHeader : "anonymous";

        SearchResult result = noteSearchService.search(cacheKey, authHeader, query == null ? "" : query);
        return ResponseEntity.ok(result);
    }
}
