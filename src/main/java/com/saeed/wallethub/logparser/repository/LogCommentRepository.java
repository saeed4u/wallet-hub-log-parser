package com.saeed.wallethub.logparser.repository;

import com.saeed.wallethub.logparser.domain.model.LogComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogCommentRepository extends JpaRepository<LogComment,Long> {
}
