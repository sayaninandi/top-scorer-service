package com.intuit.topscorerservice.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Optional;

@Component
public class OffsetDateTimeMapper {
    @Autowired
    private Optional<Calendar> calendar;

    public OffsetDateTime mapColumn(ResultSet rs, String columnLabel) throws SQLException {
        return convertToOffsetDateTime(rs.getTimestamp(columnLabel));
    }

    private OffsetDateTime convertToOffsetDateTime(Timestamp timestamp) {
        if(timestamp == null) return null;
        Optional<ZoneId> zoneId = this.calendar.flatMap((c) -> Optional.of(c.getTimeZone().toZoneId()));
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), zoneId.orElse(ZoneId.systemDefault()));
    }
}
