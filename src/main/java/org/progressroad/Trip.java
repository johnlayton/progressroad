package org.progressroad;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import static org.progressroad.Tap.Type.OFF;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Trip.Status.CANCELLED;
import static org.progressroad.Trip.Status.COMPLETED;
import static org.progressroad.Trip.Status.INCOMPLETE;
import static org.progressroad.Trip.Status.INVALID;

public record Trip(Tap on, Tap off) {

    public Status status() {
        if (Objects.isNull(on) || on.type() == OFF) {
            return INVALID;
        } else if (Objects.nonNull(off) && off.type() == ON) {
            return INVALID;
        } else if (Objects.isNull(off)) {
            return INCOMPLETE;
        } else if (Objects.equals(on.stopId(), off.stopId())) {
            return CANCELLED;
        } else {
            return COMPLETED;
        }
    }

    public Optional<Duration> duration() {
        return switch (status()) {
            case CANCELLED, COMPLETED -> Optional.of(Duration.between(on.at(), off.at()));
            case INCOMPLETE, INVALID -> Optional.empty();
        };
    }

    public int cost(final Billing billing) throws MissingBillingInformationException {
        return switch (status()) {
            case COMPLETED -> billing.minimumFare(on, off);
            case INCOMPLETE -> billing.maximumFare(on);
            case CANCELLED, INVALID -> 0;
        };
    }

    public boolean matchesTap(Tap tap) {
        return switch(tap.type()) {
            case ON -> Objects.isNull(on()) && Objects.nonNull(off())
                    && Objects.equals(off().pan(), tap.pan())
                    && Objects.equals(off().companyId(), tap.companyId())
                    && Objects.equals(off().busId(), tap.busId());
            case OFF -> Objects.isNull(off()) && Objects.nonNull(on())
                    && Objects.equals(on().pan(), tap.pan())
                    && Objects.equals(on().companyId(), tap.companyId())
                    && Objects.equals(on().busId(), tap.busId());
        };
    }

    public enum Status {CANCELLED, COMPLETED, INCOMPLETE, INVALID}
}
