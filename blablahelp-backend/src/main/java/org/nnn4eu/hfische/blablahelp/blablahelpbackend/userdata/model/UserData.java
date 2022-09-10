package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Review;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "userdata")
public class UserData {
    @Id
    private final String accountId;
    private final String firstname;
    @Version
    private Long version;
    private Set<AddressWrap> usedAddresses=new HashSet<>();

    private String motto = "Jede Woche eine neue Welt";
    private int shoppingCancellation=0;
    private int shoppingCount=0;
    private int shoppingRating=0;
    private int mitShoppingCancellation=0;
    private int mitShoppingCount=0;
    private int mitShoppingRating=0;
    private List<Review> reviewsForMitshopping=new ArrayList<>();
    private List<Review> reviewsForShopping=new ArrayList<>();
}
