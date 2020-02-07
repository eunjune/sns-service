package com.github.prgrms.social.api.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateTimeUtils {

    // LocalDateTime => Timestamp
    public static Timestamp timestampOf(LocalDateTime time) {
        return time == null ? null : Timestamp.valueOf(time);
    }

    // Timestamp => LocalDateTime
    public static LocalDateTime dateTimeOf(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

}
