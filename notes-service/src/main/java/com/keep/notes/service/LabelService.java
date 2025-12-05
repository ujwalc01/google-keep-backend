package com.keep.notes.service;

import com.keep.notes.dto.LabelCreateRequest;
import com.keep.notes.dto.LabelResponse;
import com.keep.notes.dto.LabelUpdateRequest;
import java.util.List;

public interface LabelService {

    LabelResponse createLabel(Long ownerId, LabelCreateRequest request);

    List<LabelResponse> getLabels(Long ownerId);

    LabelResponse updateLabel(Long ownerId, Long labelId, LabelUpdateRequest request);

    void deleteLabel(Long ownerId, Long labelId);
}
