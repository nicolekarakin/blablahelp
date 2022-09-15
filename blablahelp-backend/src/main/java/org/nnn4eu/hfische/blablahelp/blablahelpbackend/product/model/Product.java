package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @NotBlank
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String title;
    @NotNull
    private EProductStatus status;
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant createdAt;
    @NotEmpty
    private Set<ECategory> category;
}
