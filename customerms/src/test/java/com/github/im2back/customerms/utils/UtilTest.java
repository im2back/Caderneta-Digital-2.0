package com.github.im2back.customerms.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void testConvertDate() {
        // Arrange
        Instant instant = Instant.parse("2024-08-27T12:00:00Z"); 
        // Act
        String formattedDate = Util.convertDate(instant);

        // Assert
        assertEquals("27/08/2024 09:00", formattedDate);
    }

    @Test
    void testConvertDate2() {
        // Arrange
        Instant instant = Instant.parse("2024-08-27T12:00:00Z"); 

        // Act
        String formattedDate = Util.convertDate2(instant);

        // Assert
        assertEquals("27/08/2024", formattedDate);
    }
}
