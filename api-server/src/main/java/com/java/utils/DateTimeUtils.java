package com.java.utils;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    public static final int OFFSET_HOURS = 3;
    public static final DateTimeFormatter VERBOSE_DATETIME = DateTimeFormatter.ofPattern("HH:mm dd.MM.uuuu");

    private DateTimeUtils() {
    }

    public static Timestamp parseFrom(OffsetDateTime dateTime) {
        return dateTime == null ? null : Timestamp.valueOf(
            dateTime.atZoneSameInstant(ZoneOffset.ofHours(OFFSET_HOURS)).toLocalDateTime()
        );
    }

    public static OffsetDateTime parseFrom(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().atOffset(ZoneOffset.ofHours(OFFSET_HOURS));
    }
}
