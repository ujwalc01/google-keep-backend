package com.keep.notes.service.Impl;

import com.keep.notes.dto.LabelCreateRequest;
import com.keep.notes.dto.LabelResponse;
import com.keep.notes.dto.LabelUpdateRequest;
import com.keep.notes.entity.Label;
import com.keep.notes.exception.NotFoundException;
import com.keep.notes.repository.LabelRepository;
import com.keep.notes.service.LabelService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    @Transactional
    public LabelResponse createLabel(Long ownerId, LabelCreateRequest request) {
        if (labelRepository.existsByOwnerIdAndNameIgnoreCaseAndDeletedFalse(ownerId, request.getName())) {
            throw new DataIntegrityViolationException("Label name already exists for this user");
        }

        Label label = Label.builder()
                .ownerId(ownerId)
                .name(request.getName().trim())
                .color(request.getColor())
                .build();

        Label saved = labelRepository.save(label);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelResponse> getLabels(Long ownerId) {
        return labelRepository.findByOwnerIdAndDeletedFalseOrderByNameAsc(ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabelResponse updateLabel(Long ownerId, Long labelId, LabelUpdateRequest request) {
        Label label = labelRepository.findByIdAndOwnerIdAndDeletedFalse(labelId, ownerId)
                .orElseThrow(() -> new NotFoundException("LABEL_NOT_FOUND", "Label not found"));

        if (request.getName() != null) {
            String newName = request.getName().trim();
            if (!newName.equalsIgnoreCase(label.getName()) &&
                    labelRepository.existsByOwnerIdAndNameIgnoreCaseAndDeletedFalse(ownerId, newName)) {
                throw new DataIntegrityViolationException("Label name already exists for this user");
            }
            label.setName(newName);
        }

        if (request.getColor() != null) {
            label.setColor(request.getColor());
        }

        Label saved = labelRepository.save(label);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteLabel(Long ownerId, Long labelId) {
        Label label = labelRepository.findByIdAndOwnerIdAndDeletedFalse(labelId, ownerId)
                .orElseThrow(() -> new NotFoundException("LABEL_NOT_FOUND", "Label not found"));

        label.setDeleted(true);
        labelRepository.save(label);
    }

    private LabelResponse toResponse(Label label) {
        return new LabelResponse(label.getId(), label.getName(), label.getColor());
    }
}
