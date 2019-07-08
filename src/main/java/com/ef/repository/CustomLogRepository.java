package com.ef.repository;

import com.ef.domain.model.LogComment;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomLogRepository {

	List<LogComment> findLogsBetweenDatesWithThreshold(LocalDateTime startDateTime, LocalDateTime endDateTime, long threshold);

}
