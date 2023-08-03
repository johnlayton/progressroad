package org.progressroad;

import java.util.function.Function;

public interface TapsReader {

    String header();

    Function<String, Tap> func();

}
