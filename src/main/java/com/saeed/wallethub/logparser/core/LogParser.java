package com.saeed.wallethub.logparser.core;

import com.saeed.wallethub.logparser.core.service.LogParserService;
import com.saeed.wallethub.logparser.domain.enums.Duration;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class LogParser implements CommandLineRunner {

	@Autowired
	private LogParserService parserService;


	@Override
	public void run(String... args){
		try {
			parserService.extractLogs(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
