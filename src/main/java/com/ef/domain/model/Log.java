package com.ef.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "logs")
@Table(name = "logs", indexes = @Index(columnList = "ip,requestDateTime"))
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime requestDateTime;
	private String ip;
	private String request;
	private String requestStatusCode;
	private String userAgent;



}
