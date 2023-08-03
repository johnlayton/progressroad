package org.progressroad;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.progressroad.Tap.Type;
import org.progressroad.Trip.Status;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.progressroad.Fixtures.tap;
import static org.progressroad.Tap.Type.OFF;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Trip.Status.COMPLETED;
import static org.progressroad.Trip.Status.INCOMPLETE;

public class TapCollectorTest {

    @Test
    void shouldCreateTripFromTapOnAndTapOff() {
        final TapCollector collector = new TapCollector();

        collector.addTap(tap(ON, "Stop1"));
        collector.addTap(tap(OFF, "Stop2"));

        assertThat(collector.getTrips(), hasItem(matchingTrip(COMPLETED, ON, "Stop1", OFF, "Stop2")));
    }

    @Test
    void shouldCreateTripFromTapOnAndTapOffDifferentPans() {
        final TapCollector collector = new TapCollector();

        collector.addTap(tap(ON, "Stop1", "2222222222222222"));
        collector.addTap(tap(ON, "Stop3", "1111111111111111"));
        collector.addTap(tap(OFF, "Stop2", "1111111111111111"));

        assertThat(collector.getTrips(), hasItem(matchingTrip(COMPLETED, ON, "Stop3", OFF, "Stop2")));
    }

    @Test
    void shouldCreateTripFromTapOnAndTapOffOverlappingStarts() {
        final TapCollector collector = new TapCollector();

        collector.addTap(tap(ON, "Stop1", OffsetDateTime.parse("2023-08-03T14:00:00Z"), "1111111111111111"));
        collector.addTap(tap(ON, "Stop3", OffsetDateTime.parse("2023-08-03T11:00:00Z"), "1111111111111111"));
        collector.addTap(tap(OFF, "Stop2", OffsetDateTime.parse("2023-08-03T12:30:00Z"), "1111111111111111"));

        assertThat(collector.getTrips(), hasItem(matchingTrip(COMPLETED, ON, "Stop3", OFF, "Stop2")));
        assertThat(collector.getTrips(), hasItem(matchingTrip(INCOMPLETE, ON, "Stop1")));
    }

    @Test
    void shouldCreateMultipleTripsFromTapOnAndTapOff() {
        final TapCollector collector = new TapCollector();

        collector.addTap(tap(ON, "Stop1", OffsetDateTime.parse("2023-08-03T14:00:00Z"), "1111111111111111"));
        collector.addTap(tap(ON, "Stop3", OffsetDateTime.parse("2023-08-03T11:00:00Z"), "1111111111111111"));
        collector.addTap(tap(OFF, "Stop2", OffsetDateTime.parse("2023-08-03T12:30:00Z"), "1111111111111111"));
        collector.addTap(tap(OFF, "Stop3", OffsetDateTime.parse("2023-08-03T14:30:00Z"), "1111111111111111"));

        assertThat(collector.getTrips(), hasItem(matchingTrip(COMPLETED, ON, "Stop3", OFF, "Stop2")));
        assertThat(collector.getTrips(), hasItem(matchingTrip(COMPLETED, ON, "Stop1", OFF, "Stop3")));
    }

    private static Matcher<Trip> matchingTrip(final Status status,
                                              final Type on, final String start,
                                              final Type off, final String end) {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(final Description description) {
            }

            @Override
            public boolean matchesSafely(final Trip trip) {
                return trip.status().equals(status)
                        && trip.on().type().equals(on) && trip.on().stopId().equals(start)
                        && trip.off().type().equals(off) && trip.off().stopId().equals(end);
            }
        };
    }

    private static Matcher<Trip> matchingTrip(final Status status,
                                              final Type on, final String start) {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(final Description description) {
            }

            @Override
            public boolean matchesSafely(final Trip trip) {
                return trip.status().equals(status)
                        && trip.on().type().equals(on) && trip.on().stopId().equals(start);
            }
        };
    }
}
