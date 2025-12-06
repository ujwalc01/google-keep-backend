package com.keep.search.service;

import com.keep.search.dto.NoteSummary;
import com.keep.search.dto.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NoteSearchService {

    private final RestTemplate restTemplate;
    private final String notesServiceBaseUrl;

    public NoteSearchService(RestTemplate restTemplate,
                             @Value("${notes.service.base-url}") String notesServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.notesServiceBaseUrl = notesServiceBaseUrl;
    }

    @Cacheable(cacheNames = "notesSearch", key = "#cacheKey + ':' + #query")
    public SearchResult search(String cacheKey, String authHeader, String query) {
        List<NoteSummary> allNotes = fetchUserNotes(authHeader);
        String trimmedQuery = query == null ? "" : query.trim();

        if (trimmedQuery.isEmpty()) {
            SearchResult result = new SearchResult();
            result.setQuery(trimmedQuery);
            result.setNotes(allNotes);
            result.setTotal(allNotes.size());
            return result;
        }

        String lower = trimmedQuery.toLowerCase(Locale.ROOT);

        List<NoteSummary> filtered = allNotes.stream()
                .filter(note -> matches(note, lower))
                .collect(Collectors.toList());

        SearchResult result = new SearchResult();
        result.setQuery(trimmedQuery);
        result.setNotes(filtered);
        result.setTotal(filtered.size());
        return result;
    }

    private List<NoteSummary> fetchUserNotes(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        if (authHeader != null && !authHeader.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = notesServiceBaseUrl + "/api/v1/notes";

        ResponseEntity<NoteSummary[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NoteSummary[].class
        );

        NoteSummary[] body = response.getBody();
        if (body == null || body.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(body);
    }

    private boolean matches(NoteSummary note, String lowerQuery) {
        if (note == null) {
            return false;
        }

        String title = note.getTitle();
        if (title != null && title.toLowerCase(Locale.ROOT).contains(lowerQuery)) {
            return true;
        }

        String content = note.getContent();
        if (content != null && content.toLowerCase(Locale.ROOT).contains(lowerQuery)) {
            return true;
        }

        return false;
    }
}
