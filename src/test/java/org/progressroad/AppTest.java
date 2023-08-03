package org.progressroad;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    void shouldAcceptFilesOnConstructionAndValidate() {
        final String taps = AppTest.class.getResource("app").getPath() + "/taps.csv";
        final String trips = AppTest.class.getResource("app").getPath() + "/trips.csv";
        final App app = new App(taps, trips);

        assertTrue(app.validateInput());
    }

    @Test
    void shouldCreateCalculatorAndGenerateTrips() throws IOException {
        final String trips = Files.createTempDirectory("apptest").toFile().getPath() + "/trips.csv";
        final String taps = AppTest.class.getResource("app").getPath() + "/taps.csv";

        new App(taps, trips).generateTripsFile();

        assertTrue(new File(trips).exists());
        assertTrue(new File(trips).canRead());

        final List<String> output = Files.readAllLines(new File(trips).toPath(), Charset.defaultCharset());
        assertThat(output, hasItem(matchesRegex(
                Pattern.compile("^Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status$"))));
        assertThat(output, hasItem(matchesRegex(
                Pattern.compile("^22-01-2023 13:00:00, 22-01-2023 13:05:00, 300, Stop1, Stop2, \\$3.25, Company1, Bus37, 5500005555555559, COMPLETED$"))));

    }
}
