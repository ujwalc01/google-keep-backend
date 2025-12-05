package com.keep.notes.controller;

import com.keep.notes.dto.LabelCreateRequest;
import com.keep.notes.dto.LabelResponse;
import com.keep.notes.dto.LabelUpdateRequest;
import com.keep.notes.security.JwtUserPrincipal;
import com.keep.notes.service.LabelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public ResponseEntity<LabelResponse> createLabel(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody LabelCreateRequest request
    ) {
        LabelResponse response = labelService.createLabel(principal.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LabelResponse>> getLabels(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        List<LabelResponse> labels = labelService.getLabels(principal.getUserId());
        return ResponseEntity.ok(labels);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelResponse> updateLabel(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody LabelUpdateRequest request
    ) {
        LabelResponse response = labelService.updateLabel(principal.getUserId(), id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        labelService.deleteLabel(principal.getUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
