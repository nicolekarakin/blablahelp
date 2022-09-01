package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

public record Address(@NotBlank String street, @NotBlank String zip, @NotBlank String city, @Nullable Loc loc,@Nullable String country) {
}
