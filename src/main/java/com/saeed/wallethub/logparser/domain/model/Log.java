package com.saeed.wallethub.logparser.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity(name = "logs")
@Table(name = "logs")
public class Log {

	@Id
	@GeneratedValue
	private Long id;
	private LocalDateTime requestDateTime;
	private String ip;
	private String requestMethod;
	private String requestStatusCode;
	private String userAgent;


}
