package org.progressroad;

import org.junit.jupiter.api.Test;
import org.progressroad.Tap.Type;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private static Tap create(final Type type, final String stopId) {
        return new Tap(
                1,
                OffsetDateTime.now(),
                type,
                stopId,
                "companyId",
                "busId",
                "pan"
        );
    }
}
