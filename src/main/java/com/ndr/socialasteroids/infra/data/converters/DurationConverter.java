package com.ndr.socialasteroids.infra.data.converters;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DurationConverter implements AttributeConverter<Duration, Long>
{

    @Override
    public Long convertToDatabaseColumn(Duration duration)
    {
        return duration.toMillis();
    }

    @Override
    public Duration convertToEntityAttribute(Long duration)
    {
        return Duration.of(duration, ChronoUnit.MILLIS);
    }
    
}
