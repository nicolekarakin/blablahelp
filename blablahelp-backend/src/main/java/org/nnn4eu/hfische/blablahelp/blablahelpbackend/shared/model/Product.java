package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    @NotBlank
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String title;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private EUnit unit;
    @NotEmpty
    private Set<ECategory> category;
}
