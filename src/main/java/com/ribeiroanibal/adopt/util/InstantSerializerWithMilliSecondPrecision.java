package com.ribeiroanibal.adopt.util;

import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.format.DateTimeFormatterBuilder;

/**
 * This class has a purpose to help ObjectMapperCustom serialize/deserialize Instant types
 */
public class InstantSerializerWithMilliSecondPrecision extends InstantSerializer {

    public InstantSerializerWithMilliSecondPrecision() {
        super(InstantSerializer.INSTANCE, false, new DateTimeFormatterBuilder().appendInstant(3).toFormatter());
    }
}
