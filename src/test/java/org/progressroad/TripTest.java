package org.progressroad;

import org.junit.jupiter.api.Test;
import org.progressroad.Tap.Type;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Tap.Type.OFF;
import static org.progressroad.Trip.Status.*;

public class TripTest {

    @Test
    void shouldReportTripCompleteForTapOnAndTapOffAtDifferentStops() {
        assertEquals(COMPLETED, new Trip(create(ON, "stopId1"), create(OFF, "stopId2")).status());
    }

    @Test
    void shouldReportTripCancelledForTapOnAndTapOffAtSameStops() {
        assertEquals(CANCELLED, new Trip(create(ON, "stopId1"), create(OFF, "stopId1")).status());
    }

    @Test
    void shouldReportTripIncompleteForTapOnWithMissingTapOff() {
        assertEquals(INCOMPLETE, new Trip(create(ON, "stopId1"), null).status());
    }

    @Test
    void shouldReportTripInvalidWhenMissingTapOn() {
        assertEquals(INVALID, new Trip(null, create(OFF, "stopId1")).status());
    }

    @Test
    void shouldReportTripInvalidWhenTapOnIsRecordedAsTapOff() {
        assertEquals(INVALID, new Trip(create(OFF, "stopId1"), null).status());
    }

    @Test
    void shouldReportTripInvalidWhenTapFFIsRecordedAsTapOn() {
        assertEquals(INVALID, new Trip(create(ON, "stopId1"), create(ON, "stopId1")).status());
    }

    @Test
    void shouldReportDurationForCompletedTrip() {
        final Optional<Duration> duration = new Trip(
                create(ON, "stopId1", OffsetDateTime.parse("2023-08-03T12:00:00Z")),
                create(OFF, "stopId2", OffsetDateTime.parse("2023-08-03T13:00:00Z")))
                .duration();
        assertTrue(duration.isPresent());
        assertEquals(3600L, duration.map(Duration::getSeconds).get());
    }

    @Test
    void shouldReportDurationForIncompleteTrip() {
        final Optional<Duration> duration = new Trip(
                create(ON, "stopId1", OffsetDateTime.parse("2023-08-03T12:00:00Z")),
                null)
                .duration();
        assertFalse(duration.isPresent());
    }

    private static Tap create(final Type type, final String stopId) {
        return create(type, stopId, OffsetDateTime.now());
    }

    private static Tap create(final Type type, final String stopId, final OffsetDateTime now) {
        return new Tap(
                1,
                now,
                type,
                stopId,
                "companyId",
                "busId",
                "pan"
        );
    }
}
