package com.ef;

import com.ef.core.service.LogParserService;
import com.ef.domain.model.LogComment;
import com.ef.repository.LogCommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ParserTests {

	@Autowired
	private LogParserService parserService;

	@Autowired
	private LogCommentRepository logCommentRepository;

	@Test
	public void testExtractLogsExpectSuccess() throws IOException {
		String[] args = new String[]{"--startDate=2017-01-01.00:00:00", "--duration=daily", "--threshold=500", "--accesslog=/Users/brasaeed/Downloads/Java_MySQL_Test/access.log"};
		List<LogComment> logComments = parserService.extractLogs(args);
		assertThat(logComments).isNotEmpty();

		List<LogComment> logCommentsFromDB = logCommentRepository.findAll();
		assertThat(logCommentsFromDB.size()).isEqualTo(logComments.size());
		assertThat(logCommentsFromDB).isEqualTo(logComments);
	}

	@Test
	public void testExtractLogsExpectEmptyList() throws IOException {
		String[] args = new String[]{"--duration=daily", "--threshold=500", "--accesslog=/Users/brasaeed/Downloads/Java_MySQL_Test/access.log"};
		List<LogComment> logComments = parserService.extractLogs(args);
		assertThat(logComments).isEmpty();

		List<LogComment> logCommentsFromDB = logCommentRepository.findAll();
		assertThat(logCommentsFromDB).isEmpty();
	}

}
