package com.ef.repository;

import com.ef.domain.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long>, CustomLogRepository {
}
