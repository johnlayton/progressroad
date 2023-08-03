package org.progressroad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    void shouldAcceptFilesOnConstructionAndValidate() {
        final App app = new App(
                AppTest.class.getResource("app").getPath() + "/taps.csv",
                AppTest.class.getResource("app").getPath() + "/trips.csv"
        );

        assertTrue(app.validateInput());
    }
}
