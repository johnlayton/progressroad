package org.progressroad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Function;

public class TripsWriterImpl implements TripsWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripsWriterImpl.class);

    private final DateTimeFormatter formatter;
    private final Billing billing;

    public TripsWriterImpl(DateTimeFormatter formatter, Billing billing) {
        this.formatter = formatter;
        this.billing = billing;
    }

    @Override
    public String header() {
        return "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status";
    }

    @Override
    public Function<Trip, String> func() {
        return trip -> {
            final String formatted = "%s, %s, %d, %s, %s, $%.2f, %s, %s, %s, %s".formatted(
                    Objects.nonNull(trip.on()) ? trip.on().at().format(formatter) : "",
                    Objects.nonNull(trip.off()) ? trip.off().at().format(formatter) : "",
                    trip.duration().map(Duration::getSeconds).orElse(0L),
                    Objects.nonNull(trip.on()) ? trip.on().stopId() : "",
                    Objects.nonNull(trip.off()) ? trip.off().stopId() : "",
                    Objects.nonNull(trip.on()) ? (double) trip.cost(billing) / 100 : 0.0,
                    Objects.nonNull(trip.on()) ? trip.on().companyId() : "",
                    Objects.nonNull(trip.on()) ? trip.on().busId() : "",
                    Objects.nonNull(trip.on()) ? trip.on().pan() : "",
                    trip.status()
            );
            LOGGER.info(formatted);
            return formatted;
        };
    }
}
