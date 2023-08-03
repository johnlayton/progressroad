package org.progressroad;

import java.util.Objects;

public record Trip(Tap on, Tap off) {

    public Status status() {
        if (Objects.nonNull(on) && Objects.nonNull(off)) {
            return Objects.equals(on.stopId(), off.stopId()) ? Status.CANCELLED : Status.COMPLETED;
        } else if (Objects.nonNull(on)) {
            return Status.INCOMPLETE;
        } else {
            return Status.INVALID;
        }
    }

    public enum Status {CANCELLED, COMPLETED, INCOMPLETE, INVALID}
}
