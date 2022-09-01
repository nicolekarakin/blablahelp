package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.*;
import java.util.Set;

public class CreateData {


    public static Account createAccount() {
        return new Account(
                new BCryptPasswordEncoder().encode("blafr22"),
                "frank@gmail.de", "frank", "Berlin",
                Set.of(ERole.BASIC),
                true
        );
    }

    public static Offer changeDate(Offer offer, LocalDate date){
        LocalDateTime from=LocalDateTime.of(date, LocalTime.of(16,15));
        LocalDateTime to=LocalDateTime.of(date, LocalTime.of(20,30));
        Long day=date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Long fromL=from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long toL=to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        offer.setShoppingDay(day);
        offer.setTimeFrom(fromL);
        offer.setTimeTo(toL);
        return offer;
    }
    public static Offer createOffer(String accountId) throws JsonProcessingException {
        String str=
                """
                {
                        "accountId":"%s",
                        "shoppingDay":1661896800000,
                        "timeFrom":1661911200000,
                        "timeTo":1661926500000,
                        "shopname":"LIDL",
                        "shopAddress":{
                        "street":"Balanstraße 188",
                            "zip":"81549",
                            "city":"München",
                            "loc":null
                },
                    "destinationAddress":{
                        "city":"München",
                         "country":"DE",
                         "street":"Kaspar-Spät-Straße 20 A",
                         "zip":"81549"
                },
                    "maxMitshoppers":1,
                        "maxLiter":1,
                        "maxArticles":10,
                        "maxDistanceKm":1,
                        "priceOffer":"0"
                }
                """;
        String jsonString=str.formatted(accountId);
        return new ObjectMapper().readValue(jsonString,Offer.class);
    }
}
