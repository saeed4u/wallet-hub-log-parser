package com.ef.core.service;

import com.ef.domain.ParsedOptions;
import com.ef.domain.enums.Duration;
import com.ef.domain.model.Log;
import com.ef.domain.model.LogComment;
import com.ef.repository.LogCommentRepository;
import com.ef.repository.LogRepository;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class LogParserService {


	private Logger logger = LoggerFactory.getLogger(LogParserService.class);
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd.HH:mm:ss";

	private static final String ACCESS_LOG = "accesslog";
	private static final String START_DATE = "startDate";
	private static final String DURATION = "duration";
	private static final String THRESHOLD = "threshold";

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

	private LogRepository logRepository;

	private LogCommentRepository commentRepository;

	public LogParserService(LogRepository logRepository, LogCommentRepository commentRepository) {
		this.logRepository = logRepository;
		this.commentRepository = commentRepository;
	}



	public List<LogComment> extractLogs(String... args) throws IOException {

		ParsedOptions parsedOptions = parseArgOptions(args);
		if (parsedOptions == null) {
			return Collections.emptyList();
		}
		FileInputStream inputStream = parsedOptions.getLogFileInputStream();
		Scanner scanner = new Scanner(inputStream, "UTF-8");
		List<Log> logs = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String[] splittedLine = scanner.nextLine().split("\\|");
			if (splittedLine.length != 5) {
				continue;
			}
			Log log = parseLog(splittedLine);
			logs.add(log);
		}
		logRepository.saveAll(logs);

		inputStream.close();
		if (scanner.ioException() != null) {
			throw scanner.ioException();
		}
		List<LogComment> foundLogs = logRepository.findLogsBetweenDatesWithThreshold(parsedOptions.getStartDate(), parsedOptions.getEndDate(), parsedOptions.getThreshold())
				.parallelStream().peek(logComment -> {
					logComment.setStartDate(parsedOptions.getStartDate());
					logComment.setEndDate(parsedOptions.getEndDate());
				}).collect(Collectors.toList());

		return commentRepository.saveAll(foundLogs);
	}

	private Log parseLog(String[] splittedLine) throws DateTimeParseException {
		Log log = new Log();
		LocalDateTime startDateTime = LocalDateTime.parse(splittedLine[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		log.setRequestDateTime(startDateTime);
		log.setIp(splittedLine[1]);
		log.setRequest(splittedLine[2]);
		log.setRequestStatusCode(splittedLine[3]);
		log.setUserAgent(splittedLine[4]);
		return log;
	}

	private ParsedOptions parseArgOptions(String... args) throws IOException {
		Options options = new Options();
		//access log path
		options.addOption(getOption("a", ACCESS_LOG, "Path to access log"));
		options.addOption(getOption("s", START_DATE, String.format("Start date and time of request. Format is %s", DATE_TIME_FORMAT)));
		options.addOption(getOption("d", DURATION, "Duration to use for filtering. Can be either hourly or daily"));
		options.addOption(getOption("t", THRESHOLD, "Minimum number of requests coming from the same IP. Should be an integer"));

		CommandLineParser commandLineParser = new DefaultParser();
		String startDate = "";
		FileInputStream inputStream;
		try {
			CommandLine commandLine = commandLineParser.parse(options, args);
			if (!commandLine.hasOption(ACCESS_LOG) ||
					!commandLine.hasOption(START_DATE) ||
					!commandLine.hasOption(DURATION) ||
					!commandLine.hasOption(THRESHOLD)) {
				showHelp(options);
				return null;
			}

			Duration duration = Duration.parse(commandLine.getOptionValue("duration"));
			if (duration == null) {
				showHelp(options);
			}

			startDate = commandLine.getOptionValue("startDate");
			LocalDateTime startDateTime = LocalDateTime.parse(startDate, dateTimeFormatter);

			String logPath = commandLine.getOptionValue("accesslog");
			inputStream = new FileInputStream(logPath);
			int threshold = Integer.parseInt(commandLine.getOptionValue("threshold"));
			return ParsedOptions.newBuilder()
					.duration(duration)
					.logFileInputStream(inputStream)
					.startDate(startDateTime)
					.threshold(threshold)
					.build();

		} catch (DateTimeParseException dateTimeException) {
			logger.info("Error parsing passed date {}. Exception {}", startDate);
			showHelp(options);
		} catch (ParseException e) {
			logger.info("Error parsing arguments {}. Exception {}", args, e);
			showHelp(options);
		} catch (NumberFormatException e) {
			logger.info("Error parsing threshold {}. Exception {}", args, e);
			showHelp(options);
		} catch (FileNotFoundException e) {
			logger.info("File was not found {}. Exception {}", args, e);
			showHelp(options);
		}
		return null;
	}

	private void showHelp(Options options) {
		new HelpFormatter().printHelp("java -jar log-parser.jar", options);
	}

	private Option getOption(String opt, String longOpt, String description) {
		return new Option(opt, longOpt, true, description);
	}

}
