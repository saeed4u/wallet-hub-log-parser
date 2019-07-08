package com.ef.core;

import com.ef.core.service.LogParserService;
import com.ef.domain.model.LogComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class LogParser implements CommandLineRunner {

	@Autowired
	private LogParserService parserService;

	private Logger logger = LoggerFactory.getLogger(LogParser.class);

	@Override
	public void run(String... args) {
		try {
			List<LogComment> foundLogs = parserService.extractLogs(args);
			foundLogs.forEach(logComment -> {
				logComment.setComment(String.format("This ip(%s) made %s requests between %s and %s", logComment.getIp(), logComment.getCount(), logComment.getStartDate().toString(), logComment.getEndDate().toString()));
				System.out.println(String.format("Found Log %s", logComment));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
