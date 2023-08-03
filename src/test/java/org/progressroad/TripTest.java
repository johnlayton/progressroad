package org.progressroad;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.progressroad.Fixtures.billing;
import static org.progressroad.Fixtures.tap;
import static org.progressroad.Tap.Type.OFF;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Trip.Status.*;

public class TripTest {

    @Test
    void shouldReportTripCompleteForTapOnAndTapOffAtDifferentStops() {
        assertEquals(COMPLETED, new Trip(tap(ON, "Stop1"), tap(OFF, "Stop2")).status());
    }

    @Test
    void shouldReportTripCancelledForTapOnAndTapOffAtSameStops() {
        assertEquals(CANCELLED, new Trip(tap(ON, "Stop1"), tap(OFF, "Stop1")).status());
    }

    @Test
    void shouldReportTripIncompleteForTapOnWithMissingTapOff() {
        assertEquals(INCOMPLETE, new Trip(tap(ON, "Stop1"), null).status());
    }

    @Test
    void shouldReportTripInvalidWhenMissingTapOn() {
        assertEquals(INVALID, new Trip(null, tap(OFF, "Stop1")).status());
    }

    @Test
    void shouldReportTripInvalidWhenTapOnIsRecordedAsTapOff() {
        assertEquals(INVALID, new Trip(tap(OFF, "Stop1"), null).status());
    }

    @Test
    void shouldReportTripInvalidWhenTapFFIsRecordedAsTapOn() {
        assertEquals(INVALID, new Trip(tap(ON, "Stop1"), tap(ON, "Stop1")).status());
    }

    @Test
    void shouldReportDurationForCompletedTrip() {
        final Optional<Duration> duration = new Trip(
                tap(ON, "stopId1", OffsetDateTime.parse("2023-08-03T12:00:00Z")),
                tap(OFF, "stopId2", OffsetDateTime.parse("2023-08-03T13:00:00Z")))
                .duration();
        assertTrue(duration.isPresent());
        assertEquals(3600L, duration.map(Duration::getSeconds).get());
    }

    @Test
    void shouldReportDurationForIncompleteTrip() {
        final Optional<Duration> duration = new Trip(
                tap(ON, "Stop1", OffsetDateTime.parse("2023-08-03T12:00:00Z")),
                null)
                .duration();
        assertFalse(duration.isPresent());
    }

    @Test
    void shouldDelegateToBillingModelToCalculateCompletedTripCost() throws MissingBillingInformationException {
        assertEquals(325, new Trip(tap(ON, "Stop1"), tap(OFF, "Stop2"))
                .cost(billing()));
    }

    @Test
    void shouldDelegateToBillingModelToCalculateInCompleteTripCost() throws MissingBillingInformationException {
        assertEquals(730, new Trip(tap(ON, "Stop1"), null)
                .cost(billing()));
    }
}
