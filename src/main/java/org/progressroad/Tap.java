package org.progressroad;

import java.time.OffsetDateTime;

public record Tap(int id,
                  OffsetDateTime at,
                  Type type,
                  String stopId,
                  String companyId,
                  String busId,
                  String pan) {
    public enum Type {ON, OFF}
}
