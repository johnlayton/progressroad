package org.progressroad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.progressroad.Trip.trip;

public class TripsCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripsCalculator.class);

    private final TapsReader tapsReader;
    private final TripsWriter tripsWriter;
    private final Collector<Tap, ?, Set<Trip>> collector;
    private final Tripper tripper = new TripperImpl();

    public TripsCalculator(final TapsReader tapsReader,
                           final TripsWriter tripsWriter,
                           final Collector<Tap, ?, Set<Trip>> collector) {
        this.tapsReader = tapsReader;
        this.tripsWriter = tripsWriter;
        this.collector = collector;
    }

    public void calculate(File taps, File trips) throws IOException {
        final Stream<String> input = Files.readAllLines(taps.toPath(), Charset.defaultCharset())
                .stream()
                .peek(header -> tapsReader.header())
                .skip(1);
        Files.write(trips.toPath(),
                Stream.concat(
                        Stream.of(tripsWriter.header()),
                        calculate(input)
                ).toList(),
                Charset.defaultCharset());
    }

    public Stream<String> calculate(Stream<String> input) {
        return input.map(tapsReader.func()).flatMap(tripper).map(tripsWriter.func());
    }

    interface Tripper extends Function<Tap, Stream<Trip>> {
        default Stream<Tap> remaining() {
            return Stream.empty();
        }
    }

    public static class TripperImpl implements Tripper {

        private record Key(String pan, String companyId, String busId) {
            public Key(Tap tap) {
                this(tap.pan(), tap.companyId(), tap.busId());
            }
        }

        private Map<Key, Trip> trips = new HashMap<>();

        @Override
        public Stream<Trip> apply(Tap tap) {
            LOGGER.info("Current Trips {}", trips.size());
            LOGGER.info("Apply {}", tap);
            final Key key = new Key(tap);
            return Optional.ofNullable(trips.get(key))
                    .filter(trip -> trip.status() == Trip.Status.INCOMPLETE)
                    .map(trip -> createTripStream(trips, key, tap, trip))
                    .orElseGet(() -> {
                        trips.put(key, trip(tap));
                        return Stream.empty();
                    });
        }

        private Stream<Trip> createTripStream(Map<Key, Trip> trips, Key key, Tap tap, Trip trip) {
            return trips.remove(key, trip) ? Stream.of(trip.tap(tap)) : Stream.empty();
        }

        @Override
        public Stream<Tap> remaining() {
            return Tripper.super.remaining();
        }
    }
}
