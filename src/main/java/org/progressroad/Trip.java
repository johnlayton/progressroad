package org.progressroad;

import static org.progressroad.Trip.Status.COMPLETED;

public record Trip(Tap on, Tap off) {

    public Status status() {
        return COMPLETED;
    }

    public enum Status {CANCELLED, COMPLETED, INCOMPLETE, INVALID}
}
