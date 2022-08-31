package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model;

import lombok.*;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

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
    @NotNull
    private int maxMitshopper;
    @NotNull
    private int maxLiter;
    @NotNull
    private int maxArticles;
    @NotNull
    private int maxDistanceKm;

    private boolean isFullyBooked=false;
    private boolean isBooked=false;
    private boolean isReviewed=false;
    private boolean isCanceled=false;
    private boolean isExpired=false;

    @Setter(AccessLevel.NONE)
    private Collection<String> inquiryIds = new HashSet<>();

    public void addInquiryId(@NotBlank MitshopperInquiry inquiry){
        if(inquiryIds.size() < maxMitshopper){
            inquiryIds.add(inquiry.getInquiryId());
            inquiry.setOfferId(offerId);
            isBooked=true;
        }
        if(inquiryIds.size() == maxMitshopper){
            isFullyBooked=true;
        }
    }

    public void removeId(@NotBlank String id){
        inquiryIds.remove(id);
        if(inquiryIds.size() == 0){
            isBooked=false;
        }
        if(inquiryIds.size() < maxMitshopper){
            isFullyBooked=false;
        }
    }
}
