package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShopServiceUnitTest {

    private final ShopListRepo shopRepo = mock(ShopListRepo.class);
    @Mock
    private final ShopService shopService = new ShopService(shopRepo);


    @Test
    void getAllShopIdsForCountry() {
        Set<String> expected = Set.of("ALDI", "LIDL");
        Set<ShopList> shopList = Set.of(new ShopList("de-DE_aldi", null), new ShopList("de-DE_lidl", null));
        when(shopRepo.findByIdIgnoreCaseContaining("-DE")).thenReturn(shopList);
        Set<String> actual=shopService.findAllShopIdsForCountry("DE");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAddressesByListId() {
        String id = "de-DE_aldi";
        ShopList shopList = new ShopList(id, Set.of(new Address("Street", "12345", "City", null, null)));
        Optional<ShopList> value = Optional.of(shopList);
        when(shopRepo.findById(id)).thenReturn(value);
        Set<Address> actual = shopService.findAddressesByListId(id);
        assertThat(actual).isEqualTo(shopList.getAddresses());
    }

    @Test
    void addAddresses() throws JsonProcessingException {
        String id = "de-DE_aldi";
        Address adr1 = CreateData.createAddressWithLocNoth();
        Address adr2 = CreateData.createAddressWithLocSouth();
        Set<Address> addressHashSet = new HashSet<>(Arrays.asList(adr1, adr2));
        ShopList shopList = new ShopList(id, addressHashSet);
        Set<Address> addresses = new HashSet<>(addressHashSet);

        when(shopRepo.findById(id)).thenReturn(Optional.of(shopList));
        when(shopRepo.save(any(ShopList.class))).thenReturn(shopList);

        ShopList actual = shopService.addAddresses(id, addresses);
        Assertions.assertEquals(actual.getAddresses().size(), shopList.getAddresses().size());
    }

}
