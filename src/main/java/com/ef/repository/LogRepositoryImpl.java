package com.ef.repository;

import com.ef.domain.model.LogComment;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class LogRepositoryImpl implements CustomLogRepository {

	private EntityManager entityManager;

	public LogRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<LogComment> findLogsBetweenDatesWithThreshold(LocalDateTime startDateTime, LocalDateTime endDateTime, long threshold) {
		return entityManager
				.createQuery("SELECT new com.ef.domain.model.LogComment(ip,count(ip)) FROM logs WHERE request_date_time BETWEEN :start_date_time AND :end_date_time GROUP BY ip HAVING count(ip) >= :threshold", LogComment.class)
				.setParameter("start_date_time", startDateTime)
				.setParameter("end_date_time", endDateTime)
				.setParameter("threshold", threshold)
				.getResultList();
	}
}
