package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductWrapper {
    private Product product;
    private String note;
    private boolean isBought;

}
