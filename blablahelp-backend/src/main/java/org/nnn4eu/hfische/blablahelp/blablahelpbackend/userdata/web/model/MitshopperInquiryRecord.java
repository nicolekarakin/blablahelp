package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.ShoppingList;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MitshopperInquiryRecord(
        @NotNull String offerId,
        @NotNull String mitshopperAccountId,
        @NotNull @Valid Address mitshopperAddress,
        @NotNull BigDecimal inquiryPrice,//TODO how to handle currency? for now assume euro
        @NotNull @Valid ShoppingList shoppingList,
        String notes
) {
}
