package org.progressroad;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Billing(Set<Fare> fares) {
    public static Billing create(Set<Fare> fares) {
        return new Billing(fares.stream()
                .flatMap(fare -> Stream.of(fare, new Fare(fare.end(), fare.start(), fare.cost())))
                .collect(Collectors.toSet()));
    }

    public int minimumFare(Tap on, Tap off) throws MissingBillingInformationException {
        return fares.stream()
                .filter(fare -> fare.start().equals(on.stopId()) && fare.end().equals(off.stopId()))
                .mapToInt(Fare::cost)
                .min()
                .orElseThrow(() -> new MissingBillingInformationException(on, off));
    }

    public int maximumFare(Tap on) throws MissingBillingInformationException {
        return fares.stream()
                .filter(fare -> fare.start().equals(on.stopId()))
                .mapToInt(Fare::cost)
                .max()
                .orElseThrow(() -> new MissingBillingInformationException(on, null));
    }
}
