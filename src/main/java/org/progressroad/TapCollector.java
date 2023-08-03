package org.progressroad;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;

public class TapCollector {
    private final Set<Trip> trips = new HashSet<>();

    TapCollector() {}

    TapCollector(final Set<Trip> trips1,
                         final Set<Trip> trips2) {
        trips.addAll(trips1);
        trips.addAll(trips2);
    }

    public static Collector<Tap, ?, Set<Trip>> create() {
        return Collector.of(
                TapCollector::new,
                TapCollector::addTap,
                TapCollector::merge,
                TapCollector::getTrips,
                Collector.Characteristics.UNORDERED
        );
    }

    public static TapCollector merge(final TapCollector a,
                                     final TapCollector b) {
        return new TapCollector(a.getTrips(), b.getTrips());
    }

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
