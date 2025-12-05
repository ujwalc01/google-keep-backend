package com.keep.notes.repository;

import com.keep.notes.entity.Label;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {

    List<Label> findByOwnerIdAndDeletedFalseOrderByNameAsc(Long ownerId);

    Optional<Label> findByIdAndOwnerIdAndDeletedFalse(Long id, Long ownerId);

    boolean existsByOwnerIdAndNameIgnoreCaseAndDeletedFalse(Long ownerId, String name);
}
