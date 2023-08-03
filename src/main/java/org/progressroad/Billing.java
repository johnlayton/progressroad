package org.progressroad;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Billing(Set<Fare> fares) {
    public Billing(Set<Fare> fares) {
        this.fares = fares.stream()
                .flatMap(fare -> Stream.of(fare, new Fare(fare.end(), fare.start(), fare.cost())))
                .collect(Collectors.toSet());
    }

    public int minimumFare(Tap on, Tap off) throws MissingBillingInformationException {
        return fares.stream()
                .filter(fare -> fare.start().equals(on.stopId()) && fare.end().equals(off.stopId()))
                .mapToInt(Fare::cost)
                .min()
                .orElseThrow(() -> new MissingBillingInformationException(on, off));
    }

    public int maximumFare(Tap on) {
        return fares.stream()
                .filter(fare -> fare.start().equals(on.stopId()))
                .mapToInt(Fare::cost)
                .max()
                .orElseThrow();
    }
}
