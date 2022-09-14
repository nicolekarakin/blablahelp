package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model;

import lombok.*;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.ShoppingList;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.MitshopperInquiryRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = {"offerId", "mitshopperAccountId"})
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class MitshopperInquiry {
    @NotNull
    private String offerId;
    @NotNull
    private String mitshopperAccountId;
    @NotNull
    private String mitshopperFirstname;
    @NotNull
    @Valid
    private Address mitshopperAddress;
    @NotNull
    private BigDecimal inquiryPrice;//TODO(@nicolekarakin) how to handle currency? for now assume euro
    @NotNull
    @Valid
    private ShoppingList shoppingList;
    @NotNull
    private EInquiryStatus inquiryStatus = EInquiryStatus.AWAITING;
    private String notes;

    private boolean isDelivered = false;
    private boolean isReviewed = false;
    private boolean isCanceled = false;
    private boolean isExpired = false;

    public static MitshopperInquiry from(@Valid MitshopperInquiryRecord record) {
        return new MitshopperInquiry().toBuilder()
                .inquiryPrice(record.inquiryPrice())
                .mitshopperAccountId(record.mitshopperAccountId())
                .mitshopperFirstname(record.mitshopperFirstname())
                .mitshopperAddress(record.mitshopperAddress())
                .notes(record.notes())
                .offerId(record.offerId())
                .shoppingList(record.shoppingList())
                .build();
    }
}
