package com.trevorgowing.projectlog.common;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DateTimeServiceTests extends AbstractTests {

	private DateTimeServiceSupport dateTimeService = new DateTimeServiceSupport();

	@Test
	public void testNow_shouldReturnInstantNow() {
		// Exercise SUT
		Instant now = dateTimeService.now();

		// Verify behaviour
		assertThat(now, is(Instant.now()));
	}
}
