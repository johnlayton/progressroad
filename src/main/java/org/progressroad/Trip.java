package org.progressroad;

import java.util.Objects;

import static org.progressroad.Trip.Status.INVALID;
import static org.progressroad.Trip.Status.INCOMPLETE;
import static org.progressroad.Trip.Status.CANCELLED;
import static org.progressroad.Trip.Status.COMPLETED;
import static org.progressroad.Tap.Type.ON;
import static org.progressroad.Tap.Type.OFF;

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

    public enum Status {CANCELLED, COMPLETED, INCOMPLETE, INVALID}
}
