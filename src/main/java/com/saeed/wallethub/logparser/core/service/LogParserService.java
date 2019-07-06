package com.saeed.wallethub.logparser.core.service;

import com.saeed.wallethub.logparser.domain.ParsedOptions;
import com.saeed.wallethub.logparser.domain.enums.Duration;
import com.saeed.wallethub.logparser.domain.model.Log;
import com.saeed.wallethub.logparser.repository.LogCommentRepository;
import com.saeed.wallethub.logparser.repository.LogRepository;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Service
public class LogParserService {


	private Logger logger = LoggerFactory.getLogger(LogParserService.class);
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd.HH:mm:ss";

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

	private LogRepository logRepository;

	private LogCommentRepository commentRepository;

	public LogParserService(LogRepository logRepository, LogCommentRepository commentRepository) {
		this.logRepository = logRepository;
		this.commentRepository = commentRepository;
	}

	public void extractLogs(String... args) throws IOException {
		ParsedOptions parsedOptions = parseArgOptions(args);
		if (parsedOptions == null) {
			logger.debug("An error occurred while parsing args");
			return;
		}
		FileInputStream inputStream = parsedOptions.getLogFileInputStream();
		Scanner scanner = new Scanner(inputStream, "UTF-8");
		while (scanner.hasNextLine()) {
			String[] splittedLine = scanner.nextLine().split("\\|");
			if (splittedLine.length != 5) {
				continue;
			}
			Log log = parseLog(splittedLine);
			logger.info("Parsed log {}",log);

		}
		inputStream.close();
		if (scanner.ioException() != null) {
			throw scanner.ioException();
		}
	}

	private Log parseLog(String[] splittedLine) throws DateTimeParseException{
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
		options.addOption(getOption("a", "accesslog", "Path to access log"));
		options.addOption(getOption("s", "startDate", String.format("Start date and time of request. Format is %s", DATE_TIME_FORMAT)));
		options.addOption(getOption("d", "duration", "Duration to use for filtering. Can be either hourly or daily"));
		options.addOption(getOption("t", "threshold", "Minimum number of requests coming from the same IP. Should be an integer"));

		CommandLineParser commandLineParser = new DefaultParser();
		String startDate = "";
		FileInputStream inputStream = null;
		try {
			CommandLine commandLine = commandLineParser.parse(options, args);
			Duration duration = Duration.parse(commandLine.getOptionValue("duration"));
			if (duration == null) {
				showHelp(options);
				return null;
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
			logger.error("Error parsing passed date {}. Exception {}", startDate);
			showHelp(options);
		} catch (ParseException e) {
			logger.error("Error parsing arguments {}. Exception {}", args, e);
			showHelp(options);
		} catch (NumberFormatException e) {
			logger.error("Error parsing threshold {}. Exception {}", args, e);
			showHelp(options);
		} catch (FileNotFoundException e) {
			logger.error("File was not found {}. Exception {}", args, e);
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

	public List<Log> saveLogs(List<Log> logs) {
		return logRepository.saveAll(logs);
	}


}
