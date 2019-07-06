package com.saeed.wallethub.logparser.repository;

import com.saeed.wallethub.logparser.domain.model.Log;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomLogRepository {

	List<Log> findLogsBetweenDatesWithThreshold(LocalDateTime startDateTime, LocalDateTime endDateTime, int threshold);

}
