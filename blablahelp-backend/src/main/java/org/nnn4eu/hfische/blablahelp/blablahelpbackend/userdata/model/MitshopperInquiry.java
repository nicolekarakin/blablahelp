package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.ShoppingList;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = {"inquiryId"})
@NoArgsConstructor
public class MitshopperInquiry {
    @Id
    private String inquiryId;
    @NotNull
    @Indexed(unique = false, direction = IndexDirection.ASCENDING)
    private String offerId;
    @NotNull
    @Indexed(unique = false, direction = IndexDirection.ASCENDING)
    private String mitshopperAccountId;
    @Version
    private Long version;
    @NotNull @Valid
    private Address mitshopperAddress;
    @NotNull
    private BigDecimal inquiryPrice;//TODO how to handle currency? for now assume euro
    @NotNull @Valid
    private ShoppingList shoppingList;
    @NotNull
    private EInquiryStatus inquiryStatus = EInquiryStatus.AWAITING;
    private String notes;

    private boolean isDelivered=false;
    private boolean isReviewed=false;
    private boolean isCanceled=false;
    private boolean isExpired=false;

}
