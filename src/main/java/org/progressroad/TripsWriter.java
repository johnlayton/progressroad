package org.progressroad;

import java.util.function.Consumer;
import java.util.function.Function;

public interface TripsWriter {

    String header();

    Function<Trip, String> func();

}
