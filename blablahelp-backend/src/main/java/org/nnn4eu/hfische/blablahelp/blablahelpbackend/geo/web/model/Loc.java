package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model;

public record Loc(Double latitude, Double longitude,
                  String number,
                  String postal_code,
                  String street,
                  int confidence,
                  String label
) {
}
