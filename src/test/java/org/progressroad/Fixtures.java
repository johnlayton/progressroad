package org.progressroad;

import java.time.OffsetDateTime;

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
}
