package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model;

import lombok.*;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"offerId"})
@Document(collection = "offer")
public class Offer {
    @Id
    private String offerId;
    @Indexed(unique = false, direction = IndexDirection.ASCENDING)
    @NotBlank
    private String accountId;
    @Version
    private Long version;
    @NotNull
    private Long timeFrom;
    @NotNull
    private Long timeTo;
    @NotNull
    private Long shoppingDay;
    @NotNull
    private String shopname;
    @NotNull @Valid
    private Address shopAddress;
    @NotNull @Valid
    private Address destinationAddress;
    @NotNull
    private BigDecimal priceOffer;//TODO how to handle currency? for now assume euro
    private String notes;
    @NotNull
    private int maxMitshoppers;
    @NotNull
    private int maxLiter;
    @NotNull
    private int maxArticles;
    @NotNull
    private int maxDistanceKm;
    @Nullable
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPolygon mpolygon;

    private boolean isFullyBooked = false;
    private boolean isBooked = false;
    private boolean isReviewed = false;
    private boolean isCanceled = false;
    private boolean isExpired = false;

    @Setter(AccessLevel.NONE)
    private Collection<MitshopperInquiry> inquirys = new HashSet<>();

    public void addInquiry(@NotBlank MitshopperInquiry inquiry) {
        if (inquirys.size() < maxMitshoppers) {
            inquirys.add(inquiry);
            inquiry.setOfferId(offerId);
            isBooked = true;
        }
        if (inquirys.size() == maxMitshoppers) {
            isFullyBooked = true;
        }
    }

    public void removeInquiry(@NotBlank MitshopperInquiry inquiry) {
        inquirys.remove(inquiry);
        if (inquirys.isEmpty()) {
            isBooked = false;
        }
        if (inquirys.size() < maxMitshoppers) {
            isFullyBooked = false;
        }
    }
}
