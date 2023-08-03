package org.progressroad;

import java.time.OffsetDateTime;
import java.util.Set;

public class Fixtures {
    static Tap tap(final Tap.Type type, final String stopId) {
        return tap(type, stopId, OffsetDateTime.now());
    }

    static Tap tap(final Tap.Type type, final String stopId, final OffsetDateTime now) {
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

    static Billing billing() {
        return Billing.create(Set.of(
                new Fare("Stop1", "Stop2", 325),
                new Fare("Stop2", "Stop3", 550),
                new Fare("Stop1", "Stop3", 730)
        ));
    }
}
