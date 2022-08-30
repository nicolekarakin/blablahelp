package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingList {
    @Id
    private String accountId;
    @Version
    private Long version;
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant createdAt;
    private String title;
    private List<ProductWrapper> products;
}
