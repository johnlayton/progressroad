package org.progressroad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static net.logstash.logback.argument.StructuredArguments.v;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final File taps;
    private final File trips;

    public static void main(String[] args) {
    }

    public App(File taps, File trips) {
        this.taps = taps;
        LOGGER.info("Taps File {}", v("taps", taps));

        this.trips = trips;
        LOGGER.info("Trips File {}", v("trips", trips));
    }

    public App(String taps, String trips) {
        this(new File(taps), new File(trips));
    }

    public boolean validateInput() {
        return taps.exists() && taps.canRead() && !trips.exists();
    }
}
