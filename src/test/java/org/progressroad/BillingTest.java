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
    void shouldShouldCalculateFareForCompletedTripForward() throws Exception {
        assertEquals(325, billing().minimumFare(
                tap(ON, "Stop1"),
                tap(OFF, "Stop2")
        ));
    }

    @Test
    void shouldShouldCalculateFareForCompletedTripReversed() throws Exception  {
        assertEquals(325, billing().minimumFare(
                tap(ON, "Stop2"),
                tap(OFF, "Stop1")
        ));
    }

    @Test
    void shouldThrowExceptionWhenTapOnAtUnknownStop() {
        assertThrows(MissingBillingInformationException.class, () -> {
            billing().minimumFare(
                    tap(ON, "Stop4"),
                    tap(OFF, "Stop2")
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenTapOffAtUnknownStop() {
        assertThrows(MissingBillingInformationException.class, () -> {
            billing().minimumFare(
                    tap(ON, "Stop1"),
                    tap(OFF, "Stop4")
            );
        });
    }
}
