package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "offer")
public class Offer {
    @Id
    private String id;
    private String accountId;
    @Version
    private Long version;

    private Instant timeFrom;
    private Instant timeTo;
    private Instant shoppingDay;

    private String shopname;
    private Address shopAddress;
    private Address destinationAddress;
    private BigDecimal priceOffer;//TODO how to handle currency? for now assume euro

    private int maxMitshopper;
    private int maxLiter;
    private int maxArticles;
    private int maxDistanceKm;

    private boolean isVisible=true;
    private boolean isReviewed=false;
    private boolean isCanceled=false;
    private boolean isExpired=false;
    private boolean agbAccepted=false;

    private List<MitshopperInquiry> inquiries;

}
