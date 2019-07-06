package com.saeed.wallethub.logparser.domain;

import com.saeed.wallethub.logparser.domain.enums.Duration;
import lombok.Getter;

import java.io.FileInputStream;
import java.time.LocalDateTime;

@Getter
public class ParsedOptions {

	private int threshold;
	private Duration duration;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private FileInputStream logFileInputStream;

	public static Builder newBuilder() {
		return new ParsedOptions().new Builder();
	}

	public class Builder {
		public Builder threshold(int threshold) {
			ParsedOptions.this.threshold = threshold;
			return this;
		}

		public Builder duration(Duration duration) {
			ParsedOptions.this.duration = duration;
			return this;
		}

		public Builder startDate(LocalDateTime startDate) {
			ParsedOptions.this.startDate = startDate;
			return this;
		}

		public Builder logFileInputStream(FileInputStream logFileInputStream) {
			ParsedOptions.this.logFileInputStream = logFileInputStream;
			return this;
		}

		public ParsedOptions build() {
			switch (duration) {
				case HOURLY:
					endDate = startDate.plusHours(1L);
					break;
				case DAILY:
					endDate = startDate.plusDays(1L);
			}
			return ParsedOptions.this;
		}
	}

}
