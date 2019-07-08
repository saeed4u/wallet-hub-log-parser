package com.ef.repository;

import com.ef.domain.model.LogComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogCommentRepository extends JpaRepository<LogComment,Long> {
}
