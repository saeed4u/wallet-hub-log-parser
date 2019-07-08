package com.ef.domain.enums;

public enum Duration {

	HOURLY, DAILY;

	public static Duration parse(String duration) {
		switch (duration.toLowerCase()) {
			case "hourly":
				return HOURLY;
			case "daily":
				return DAILY;
		}
		return null;
	}

}
