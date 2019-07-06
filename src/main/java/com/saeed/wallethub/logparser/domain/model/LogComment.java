package com.saeed.wallethub.logparser.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "log_comments")
@Table(name = "log_comments")
public class LogComment {

	@Id
	private String ip;
	private Long count;
	private String comment;
}
