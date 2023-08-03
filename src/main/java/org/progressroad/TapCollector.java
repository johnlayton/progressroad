package org.progressroad;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class TapCollector {
    private final Set<Trip> trips = new HashSet<>();

    public Set<Trip> getTrips() {
        return trips;
    }

    public void addTap(final Tap tap) {
        switch (tap.type()) {
            case ON -> startTrip(tap);
            case OFF -> finishTrip(tap);
        }
    }

    private void finishTrip(Tap tap) {
        trips.stream()
                .filter(trip -> trip.matchesTap(tap))
                .filter(trip -> trip.on().at().isBefore(tap.at()))
                .min(Comparator.comparing(trip -> Duration.between(trip.on().at(), tap.at())))
                .ifPresentOrElse(
                        replaceTrip(trip -> new Trip(trip.on(), tap)),
                        addTrip(new Trip(null, tap))
                );
    }

    private void startTrip(Tap tap) {
        trips.stream()
                .filter(trip -> trip.matchesTap(tap))
                .filter(trip -> trip.off().at().isAfter(tap.at()))
                .min(Comparator.comparing(trip -> Duration.between(trip.off().at(), tap.at())))
                .ifPresentOrElse(
                        replaceTrip(trip -> new Trip(tap, trip.off())),
                        addTrip(new Trip(tap, null))
                );
    }

    private Runnable addTrip(Trip fromTapOn) {
        return () -> {
            trips.add(fromTapOn);
        };
    }

    private Consumer<Trip> replaceTrip(Function<Trip, Trip> e) {
        return trip -> {
            trips.remove(trip);
            trips.add(e.apply(trip));
        };
    }
}
