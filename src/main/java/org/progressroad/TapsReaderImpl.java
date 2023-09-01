package org.progressroad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import static org.progressroad.Tap.Type.valueOf;

public class TapsReaderImpl implements TapsReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TapsReaderImpl.class);

    private final DateTimeFormatter formatter;

    public TapsReaderImpl(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }


    @Override
    public String header() {
        return "ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN";
    }

    @Override
    public Function<String, Tap> func() {
        return entry -> {
            LOGGER.info("{}", entry);
            final String[] fields = entry.split(",");
            return new Tap(
                    Integer.parseInt(fields[0].trim()),
                    OffsetDateTime.of(LocalDateTime.parse(fields[1].trim(), formatter), ZoneOffset.UTC),
                    valueOf(fields[2].trim()),
                    fields[3].trim(),
                    fields[4].trim(),
                    fields[5].trim(),
                    fields[6].trim()
            );
        };
    }
}
