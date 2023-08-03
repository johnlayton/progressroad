package org.progressroad;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.progressroad.Fixtures.billing;
import static org.progressroad.Fixtures.tap;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Tap.Type.OFF;

public class BillingTest {

    @Test
    void shouldCalculateFareForCompletedTripForward() throws Exception {
        assertEquals(325, billing().minimumFare(
                tap(ON, "Stop1"),
                tap(OFF, "Stop2")
        ));
    }

    @Test
    void shouldCalculateFareForCompletedTripReversed() throws Exception {
        assertEquals(325, billing().minimumFare(
                tap(ON, "Stop2"),
                tap(OFF, "Stop1")
        ));
    }

    @Test
    void shouldThrowExceptionWhenTapOnAtUnknownStop() {
        final MissingBillingInformationException exception = assertThrows(MissingBillingInformationException.class, () -> {
            billing().minimumFare(
                    tap(ON, "Stop4"),
                    tap(OFF, "Stop2")
            );
        });

        assertEquals("Missing billing information for route Stop4 to Stop2", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTapOffAtUnknownStop() {
        final MissingBillingInformationException exception = assertThrows(MissingBillingInformationException.class, () -> {
            billing().minimumFare(
                    tap(ON, "Stop1"),
                    tap(OFF, "Stop4")
            );
        });
        assertEquals("Missing billing information for route Stop1 to Stop4", exception.getMessage());
    }

    @Test
    void shouldCalculateFareForIncompleteTrip() {
        assertEquals(550, billing().maximumFare(
                tap(ON, "Stop2")
        ));
        assertEquals(730, billing().maximumFare(
                tap(ON, "Stop1")
        ));
    }
}
