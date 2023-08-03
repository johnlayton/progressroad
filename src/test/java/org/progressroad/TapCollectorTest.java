package org.progressroad;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.progressroad.Fixtures.tap;
import static org.progressroad.Tap.Type.OFF;
import static org.progressroad.Tap.Type.ON;

public class TapCollectorTest {

    @Test
    void shouldCreateTripFromTapOnAndTapOff() {
        final TapCollector collector = new TapCollector();

        collector.addTap(tap(ON, "Stop1"));
        collector.addTap(tap(OFF, "Stop2"));

        assertThat(collector.getTrips(), hasItem(matchingTrip("Stop1", "Stop2")));
    }

    private static Matcher<Trip> matchingTrip(final String start,
                                              final String end) {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(final Description description) {
            }

            @Override
            public boolean matchesSafely(final Trip trip) {
                return trip.on().stopId().equals(start) &&
                        trip.off().stopId().equals(end);
            }
        };
    }
}
