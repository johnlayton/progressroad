package org.progressroad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static net.logstash.logback.argument.StructuredArguments.v;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final File taps;
    private final File trips;

    public App(File taps, File trips) {
        this.taps = taps;
        LOGGER.info("Taps File {}", v("taps", taps));

        this.trips = trips;
        LOGGER.info("Trips File {}", v("trips", trips));
    }

    public App(String taps, String trips) {
        this(new File(taps), new File(trips));
    }

    public static void main(String[] args) throws IOException {
        final App app = new App(args[0], args[1]);
        if (app.validateInput()) {
            app.generateTripsFile();
        }
    }

    public boolean validateInput() {
        LOGGER.info("Validate Files");
        return taps.exists() && taps.canRead() && !trips.exists();
    }

    public void generateTripsFile() throws IOException {
        LOGGER.info("Generate Trips");
        new TripsCalculator(
                new TapsReaderImpl(FORMATTER),
                new TripsWriterImpl(FORMATTER, Billing.create(Set.of(
                        new Fare("Stop1", "Stop2", 325),
                        new Fare("Stop2", "Stop3", 550),
                        new Fare("Stop1", "Stop3", 730)
                ))),
                TapCollector.create()
        ).calculate(taps, trips);
    }
}
