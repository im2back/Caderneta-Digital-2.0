package com.github.im2back.customerms.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void shouldConvertInstantToFormattedDate() {
		// ARRANGE
		Instant instant = Instant.parse("2025-03-15T12:30:00Z");

		OffsetDateTime expectedDateTime = instant.atOffset(ZoneOffset.ofHours(-3));
		String expectedFormattedDate = expectedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

		// ACT
		String formattedDate = Util.convertDate(instant);

		// ASSERT
		assertThat(formattedDate).isEqualTo(expectedFormattedDate);
	}

}
