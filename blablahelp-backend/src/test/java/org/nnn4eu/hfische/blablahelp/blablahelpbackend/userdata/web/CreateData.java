package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.model.*;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.MitshopperInquiryRecord;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.SearchOfferResponse;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateData {

    public static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
            .modules(new JavaTimeModule(), new Jdk8Module(), new GeoJsonModule())
            .build();

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
        Long day=LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        Long from=LocalDate.now().plusDays(1).atTime(14,15,0).toInstant(ZoneOffset.UTC).toEpochMilli();
        Long to=LocalDate.now().plusDays(1).atTime(17,15,0).toInstant(ZoneOffset.UTC).toEpochMilli();

        String str=
                """
                {
                        "accountId":"%s",
                        "shoppingDay":%d,
                        "timeFrom":%d,
                        "timeTo":%d,
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
        String jsonString = str.formatted(accountId, day, from, to);
        return objectMapper.readValue(jsonString, Offer.class);
    }

    public static Offer createOfferWithLoc(String accountId) throws JsonProcessingException {
        Long day = LocalDate.now().plusDays(3).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        Long from = LocalDate.now().plusDays(3).atTime(14, 15, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
        Long to = LocalDate.now().plusDays(3).atTime(17, 15, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
        String str =
                """
                                {
                                  "accountId":"%s",
                                  "shoppingDay":%d,
                                  "timeFrom":%d,
                                  "timeTo":%d,
                                  "shopname": "LIDL",
                                  "shopAddress": {
                                    "street": "Theresienhöhe 5",
                                    "zip": "80339",
                                    "city": "München",
                                    "loc": {
                                      "type": "Point",
                                      "coordinates": [
                                        11.546724,
                                        48.136749
                                      ]
                                    }
                                  },
                                  "destinationAddress": {
                                    "street": "Hansastraße 132",
                                    "zip": "81373",
                                    "city": "München",
                                    "loc": {
                                      "type": "Point",
                                      "coordinates": [
                                        11.534558,
                                        48.121061
                                      ]
                                    },
                                    "country": "DE"
                                  },
                                  "priceOffer": "0",
                                  "maxMitshoppers": 2,
                                  "maxLiter": 5,
                                  "maxArticles": 18,
                                  "maxDistanceKm": 5,
                                  "isFullyBooked": false,
                                  "isBooked": false,
                                  "isReviewed": false,
                                  "isCanceled": false,
                                  "isExpired": false,
                                  "inquiryIds": []
                                }
                        """;
        String jsonString = str.formatted(accountId, day, from, to);
        return objectMapper.readValue(jsonString, Offer.class);
    }

    public static Address createAddressWithLocNothEast() throws JsonProcessingException {
        String jsonString =
                """
                                {
                                      "street": "Ludwigsfelder Str. 15",
                                      "zip": "80999",
                                      "city": "München",
                                      "loc": {
                                        "type": "Point",
                                        "coordinates": [
                                          11.463639891661815,
                                          48.19931734961919
                                        ]
                                      }
                                }
                                 
                        """;

        return objectMapper.readValue(jsonString, Address.class);
    }

    public static Address createAddressWithLocNoth() throws JsonProcessingException {
        String jsonString =
                """
                                {
                                      "street": "Schleißheimer Str. 466/468",
                                      "zip": "80797",
                                      "city": "München",
                                      "loc": {
                                        "type": "Point",
                                        "coordinates": [
                                          11.581812,
                                          48.210053
                                        ]
                                      }
                                }
                                 
                        """;

        return objectMapper.readValue(jsonString, Address.class);
    }

    public static Address createAddressWithLocSouth() throws JsonProcessingException {
        String jsonString =
                """
                                {
                                      "street": "Martin-Behaim-Straße 3",
                                      "zip": "81373",
                                      "city": "München",
                                      "loc": {
                                        "type": "Point",
                                        "coordinates": [
                                          11.5372926,
                                          48.1250210
                                        ]
                                      }
                                 }
                        """;

        return objectMapper.readValue(jsonString, Address.class);
    }

    public static MitshopperInquiryRecord createMitshopperInquiryRecord(String offerId, String name,
                                                                        String mitshopperId, String title) throws JsonProcessingException {
        return new MitshopperInquiryRecord(
                offerId,
                mitshopperId,
                name,
                createAddressWithLocNoth(),
                new BigDecimal("23"),
                CreateData.createShoppingList(mitshopperId, title),
                "Some notes here"
        );
    }

    public static ShoppingList createShoppingList(String accountId, String title) {
        List<@Valid ProductWrapper> products = new ArrayList<>();

        products.add(
                ProductWrapper.builder()
                        .amount(1f)
                        .unit(EUnit.L)
                        .title("milch")
                        .build());

        products.add(
                ProductWrapper.builder()
                        .amount(1.5f).unit(EUnit.KG)
                        .title("Zwiebel")
                        .build());

        products.add(
                ProductWrapper.builder()
                        .amount(500f).unit(EUnit.G)
                        .note("Jakobs falls möglich und INTENSITÄT 3 aus 5")
                        .title("Kaffee")
                        .build());
        products.add(
                ProductWrapper.builder()
                        .amount(500f).unit(EUnit.ML)
                        .note("Schokolade, falls möglich eine von: Brands Zero, Ben&Jerry, Häagen-Dazs")
                        .title("Eiscreme")
                        .build());

        return ShoppingList.builder()
                .products(products)
                .accountId(accountId)
                .title(title)
                .build();
    }

    private static Product createAndSaveProduct(Set<ECategory> categories, String title) {
        return Product.builder()
                .title(title)
                .status(EProductStatus.APPROVED)
                .category(categories)
                .build();

    }

    public static SearchOfferResponse createSearchOfferResponse(Offer offer, UserData userData) {
        return SearchOfferResponse.from(offer, userData);
    }

    public static UserData createUserData(String accountId, String firstname) {
        return new UserData(accountId, firstname);
    }
}
