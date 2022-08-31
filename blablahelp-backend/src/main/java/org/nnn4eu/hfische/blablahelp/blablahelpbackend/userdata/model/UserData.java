package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model;

import lombok.*;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "userdata")
public class UserData {
    @Id
    private final String accountId;
    @Version
    private Long version;
    private Set<AddressWrap> usedAddresses=new HashSet<>();
}
