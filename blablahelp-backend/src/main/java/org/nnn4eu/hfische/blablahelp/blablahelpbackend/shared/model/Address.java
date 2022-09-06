package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @NotBlank
    private String street;
    @NotBlank
    private String zip;
    @NotBlank
    private String city;
    @Nullable
    private GeoJsonPoint loc;
    @Nullable
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return street.equals(address.street) && zip.equals(address.zip) && city.equals(address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, zip, city);
    }
}
