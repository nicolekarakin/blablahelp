package org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ShoppingList {
    @Id
    @NotBlank
    private String title;
    @NotBlank
    private String accountId;
    @Version
    private Long version;
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant createdAt;
    @NotEmpty
    private List<@Valid ProductWrapper> products;
}
