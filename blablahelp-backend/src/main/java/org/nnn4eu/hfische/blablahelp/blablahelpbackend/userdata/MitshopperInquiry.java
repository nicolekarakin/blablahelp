package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.ShoppingList;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MitshopperInquiry {
    @Id
    private String id;
    private String accountId;
    @Version
    private Long version;
    private Address mitshopperAddress;
    private BigDecimal inquiryPrice;//TODO how to handle currency? for now assume euro

    private ShoppingList shoppingList;
    private EInquiryStatus inquiryStatus;
    private String notes;

    private boolean isDelivered=false;
    private boolean isReviewed=false;
    private boolean isCanceled=false;
    private boolean isExpired=false;

}
