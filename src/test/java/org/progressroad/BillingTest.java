package org.progressroad;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.progressroad.Fixtures.tap;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Tap.Type.OFF;

public class BillingTest {

    @Test
    void shouldShouldCalculateFareForCompletedTrip() {

        Billing billing = new Billing(Set.of(
                new Fare("Stop1", "Stop2", 325),
                new Fare("Stop2", "Stop3", 550),
                new Fare("Stop1", "Stop3", 730)
        ));

        assertEquals(325, billing.minimumFare(
                tap(ON, "Stop1"),
                tap(OFF, "Stop2")
        ));
    }


}
