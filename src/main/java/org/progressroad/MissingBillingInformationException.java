package org.progressroad;

import java.util.Objects;

public class MissingBillingInformationException extends Exception {
    private final Tap on;
    private final Tap off;

    public MissingBillingInformationException(Tap on, Tap off) {
        this.on = on;
        this.off = off;
    }

    @Override
    public String getMessage() {
        return "Missing billing information for route %s to %s".formatted(
                Objects.nonNull(on.stopId()) ? on.stopId() : "N/A",
                Objects.nonNull(off.stopId()) ? off.stopId() : "N/A"
        );
    }
}
