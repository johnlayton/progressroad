package org.progressroad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.logstash.logback.argument.StructuredArguments.v;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Taps File {}", v("taps", args[0]));
        LOGGER.info("Trips File {}", v("trips", args[1]));
    }

}
