package com.ef.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "log_comments")
@Table(name = "log_comments")
@Getter
@ToString
@EqualsAndHashCode
public class LogComment {

	@Id
	private String ip;
	private Long count;

	@Setter
	private String comment;

	@Getter
	@Setter
	private LocalDateTime startDate;

	@Getter
	@Setter
	private LocalDateTime endDate;

	public LogComment(String ip, Long count) {
		this.ip = ip;
		this.count = count;
	}

	public LogComment() {
	}
}
