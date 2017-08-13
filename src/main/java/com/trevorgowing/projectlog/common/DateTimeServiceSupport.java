package com.trevorgowing.projectlog.common;

import java.time.Instant;

public class DateTimeServiceSupport implements DateTimeService {

	public Instant now() {
		return Instant.now();
	}
}
