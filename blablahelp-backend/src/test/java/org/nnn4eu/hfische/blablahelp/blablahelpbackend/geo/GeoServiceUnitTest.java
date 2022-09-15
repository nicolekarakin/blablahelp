package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model.ForwardResponse;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.web.model.Loc;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GeoServiceUnitTest {

    private Address adr1;
    private WebClient webClientMock;
    private final ShopService shopService = mock(ShopService.class);
    private final GeoService geoService = new GeoService(shopService);

    @BeforeAll
    void init() throws JsonProcessingException {
        adr1 = CreateData.createAddressWithLocNoth();
        Loc loc = new Loc(adr1.getLoc().getY(), adr1.getLoc().getX(),
                "87", adr1.getZip(), adr1.getStreet(), 1, "label");
        List<Loc> data = List.of(loc);
        ForwardResponse response = new ForwardResponse(data);
        String json = new ObjectMapper().writeValueAsString(response);

        webClientMock = WebClient.builder()
                .baseUrl("http://api.positionstack.com")
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body(json)
                        .build()))
                .build();
        geoService.setWebClient(webClientMock);

    }


    @Test
    void addCoordinatesToShops_throw() throws JsonProcessingException {
        String shopListId = UUID.randomUUID().toString();
        Address adr1 = CreateData.createAddressWithLocNoth();
        Address adr2 = CreateData.createAddressWithLocSouth();
        Set<Address> addresses = new HashSet<>(Arrays.asList(adr1, adr2));

        when(shopService.findAddressesByListId(shopListId)).thenReturn(addresses);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> geoService.addCoordinatesToShops(shopListId));
    }

    @Test
    void addCoordinatesToShops() throws JsonProcessingException {
        String shopListId = "de-DE_lidl";
        Address adr1 = CreateData.createAddressWithLocNoth();
        Address adr2 = CreateData.createAddressWithLocSouth();
        Set<Address> addresses = new HashSet<>(Arrays.asList(adr1, adr2));
        when(shopService.findAddressesByListId(shopListId)).thenReturn(addresses);
        geoService.addCoordinatesToShops(shopListId);
        verify(shopService, times(1)).addAddresses(shopListId, addresses);
    }

    @Test
    void getCoordinatesForAddress() {

        String addressStr = adr1.getStreet() + " " + adr1.getZip() + " " + adr1.getCity() + "DEU";
        GeoJsonPoint geoPoint = geoService.getCoordinates(addressStr);

        assertThat(geoPoint.getX()).isEqualTo(adr1.getLoc().getX());
        assertThat(geoPoint.getY()).isEqualTo(adr1.getLoc().getY());
    }

}