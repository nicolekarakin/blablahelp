package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.web.ctrl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.web.model.ShopNameList;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShopCtrlTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ShopService shopService;

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getShopNames() throws Exception {
        String id="de-DE_aldi";
        ShopNameList expected = new ShopNameList(Set.of("ALDI"));
        addShops(id);

        MvcResult mvcResult = mockMvc.perform(
                        get(UrlMapping.BASIC+"/DE/shopnames")
                ).andExpect(status().isOk())
                .andReturn();
        String actualStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ShopNameList actual = objectMapper.readValue(actualStr, ShopNameList.class);

        assertThat(actual).isEqualTo(expected);
    }

    void addShops(String id){
        Set<Address> addresses=Set.of(new Address("Street 1","12345","City",null),
                new Address("Street 2","12345","City",null),
                new Address("Some Str 33","12345","City",null));
        shopService.addAddresses(id,addresses);
    }
    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getShopAddressesByCountry() throws Exception {
        String id="de-DE_aldi";
        Set<Address> expected = Set.of(new Address("Some Str 33","12345","City",null));
        addShops(id);

        MvcResult mvcResult = mockMvc.perform(
                        get(UrlMapping.BASIC+"/de-DE/shops/aldi/addresses")
                                .param("city","City")
                                .param("search","ome")
                ).andExpect(status().isOk())
                .andReturn();
        String actualStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Set<Address> actual = objectMapper.readValue(actualStr, new TypeReference<>() {
        });

        assertThat(actual).isEqualTo(expected);
    }

}
