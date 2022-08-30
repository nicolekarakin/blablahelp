package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
//@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "userdata")
public class UserData {
    @Id
    private final String accountId;
    @Version
    private Long version;
    private Set<AddressWrap> usedAddresses=new HashSet<>();
    private List<Offer> currentOffers =new ArrayList<>();
    private List<MitshopperInquiry> currentInquiries =new ArrayList<>();
}
