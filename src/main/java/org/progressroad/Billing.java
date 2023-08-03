package org.progressroad;

import java.util.Set;

public record Billing(Set<Fare> fares) {
    public int minimumFare(Tap on, Tap off) {
        return fares.stream()
                .filter(fare -> fare.start().equals(on.stopId()) && fare.end().equals(off.stopId()))
                .mapToInt(Fare::cost)
                .min()
                .orElseThrow();
    }
}
