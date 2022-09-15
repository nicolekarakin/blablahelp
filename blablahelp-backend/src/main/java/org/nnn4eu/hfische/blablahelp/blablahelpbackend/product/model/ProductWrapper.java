package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductWrapper {
    @NotBlank
    private String title;
    @NotEmpty
    private Set<ECategory> category;
    private String note;
    @NotNull
    private float amount;
    @NotNull
    private EUnit unit;
    @NotNull
    private boolean isBought = false;


}
