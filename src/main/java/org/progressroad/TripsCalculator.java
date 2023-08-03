package org.progressroad;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class TripsCalculator {

    private final TapsReader tapsReader;
    private final TripsWriter tripsWriter;
    private final Collector<Tap, ?, Set<Trip>> collector;

    public TripsCalculator(final TapsReader tapsReader,
                           final TripsWriter tripsWriter,
                           final Collector<Tap, ?, Set<Trip>> collector) {
        this.tapsReader = tapsReader;
        this.tripsWriter = tripsWriter;
        this.collector = collector;
    }

    public void calculate(File taps, File trips) throws IOException {
        Files.write(trips.toPath(), Stream.concat(
                        Stream.of(tripsWriter.header()),
                        Files.readAllLines(taps.toPath(), Charset.defaultCharset())
                                .stream()
                                .peek(header -> tapsReader.header())
                                .skip(1)
                                .map(tapsReader.func())
                                .collect(collector)
                                .stream()
                                .map(tripsWriter.func())).toList(),
                Charset.defaultCharset());
    }
}
