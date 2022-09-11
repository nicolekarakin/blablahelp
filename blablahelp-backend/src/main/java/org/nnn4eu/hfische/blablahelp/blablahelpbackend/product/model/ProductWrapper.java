package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWrapper {
    @Valid
    private Product product;
    private String note;
    @NotNull
    private float amount;
    @NotNull
    private EUnit unit;
    @NotNull
    private boolean isBought = false;

}
