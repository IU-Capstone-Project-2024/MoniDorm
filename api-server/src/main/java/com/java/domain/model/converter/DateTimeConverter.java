package com.java.domain.model.converter;

import com.java.utils.DateTimeUtils;
import jakarta.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class DateTimeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime dateTime) {
        return DateTimeUtils.parseFrom(dateTime);
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp timestamp) {
        return DateTimeUtils.parseFrom(timestamp);
    }
}
