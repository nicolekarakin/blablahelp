package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model.ForwardResponse;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model.Loc;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RequiredArgsConstructor

@Slf4j
@Service
public class GeoService {
    @Value("${pstoken}")
    private String token;
    private final String defaultCountry = "DE";
    private final ShopService shopService;
    private final WebClient webClient = WebClient.create("http://api.positionstack.com");

    public void addCoordinatesToShops(String shopListId) {
        String alpha3Country = getIso3CountryFromShopListId(shopListId);
        log.info("shopListId: " + shopListId);
        Set<Address> addresses = shopService.findAddressesByListId(shopListId);
        addresses.forEach(a -> {

            if (a.getCountry() != null && a.getCountry().isEmpty()) a.setCountry(alpha3Country);
            if (a.getLoc() == null) {
                GeoJsonPoint point = getCoordinatesForAddress(a);
                a.setLoc(point);
            }
        });
        shopService.addAddresses(shopListId, addresses);
    }

    public GeoJsonPoint getCoordinatesForAddress(Address address) {
        String country = (address.getCountry() == null) ?
                new Locale("de", defaultCountry).getISO3Country()
                : address.getCountry();
        String addressStr = address.getStreet() + " " + address.getZip() + " " + address.getCity() + " " + country;

        return getCoordinates(addressStr);
    }

    private GeoJsonPoint getCoordinates(String addressStr) {
        String query = addressStr.replaceAll("[:.,;@$&\\\\|/]", " ");
        log.info("token ok " + (!token.isEmpty()) + ", Looking for address: " + addressStr);
        ForwardResponse response = webClient.get()
                .uri(builder -> builder.path("/v1/forward")
                        .queryParam("access_key", token)
                        .queryParam("query", query)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ForwardResponse.class)
                .log()
                .block();
        if (response != null) {
            List<Loc> locs = response.data();//TODO should we use confidence levels?
            log.info("Address to Geolocation: " + Arrays.toString(locs.toArray()));
            return new GeoJsonPoint(locs.get(0).longitude(), locs.get(0).latitude());
        } else throw new IllegalArgumentException("Couldn't fetch geocoordinates for provided address");
    }

    private String getIso3CountryFromShopListId(String shopListId) {
        String iso2;
        String[] infoStr = shopListId.split("-");
        if (infoStr.length != 2) throw new IllegalArgumentException("ShopListId is in a wrong format");
        else iso2 = infoStr[1].split("_")[0];
        return new Locale("de", iso2).getISO3Country();
    }
}
