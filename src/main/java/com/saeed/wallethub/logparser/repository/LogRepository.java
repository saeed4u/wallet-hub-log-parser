package com.saeed.wallethub.logparser.repository;

import com.saeed.wallethub.logparser.domain.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
