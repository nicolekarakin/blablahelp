package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    private String title;
    private BigDecimal amount;
    private EUnit unit;
    private Set<ECategory> category;
}
