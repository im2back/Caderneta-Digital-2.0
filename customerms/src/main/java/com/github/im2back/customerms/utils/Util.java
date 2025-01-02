package com.github.im2back.customerms.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Util {

	public static String convertDate(Instant instant) {

		OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.ofHours(-3));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = offsetDateTime.format(formatter);;
         
        return formattedDate;
	}
	
}
