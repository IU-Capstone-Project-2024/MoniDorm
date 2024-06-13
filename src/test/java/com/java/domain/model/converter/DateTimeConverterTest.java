package com.java.domain.model.converter;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DateTimeConverterTest {
    private static final DateTimeConverter CONVERTER = new DateTimeConverter();

    @Test
    void fromOffsetToTimestamp() {
        OffsetDateTime odt = OffsetDateTime.of(2024, 6, 11, 2,
            25, 0, 0, ZoneOffset.ofHours(3)
        );

        assertThat(odt.getHour()).isEqualTo(CONVERTER.convertToDatabaseColumn(odt).toInstant()
            .atOffset(ZoneOffset.ofHours(3)).getHour());
    }

    @Test
    void fromTimestampToOffset() {
        Timestamp ts = new Timestamp(2024, 6, 11, 2,
            25, 0, 0
        );

        assertThat(ts.toLocalDateTime().getHour()).isEqualTo(CONVERTER.convertToEntityAttribute(ts).toInstant()
            .atOffset(ZoneOffset.ofHours(3)).getHour());
    }
}
