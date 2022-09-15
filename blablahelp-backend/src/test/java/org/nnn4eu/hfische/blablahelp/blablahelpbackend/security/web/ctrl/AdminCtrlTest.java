package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoSearchService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.SecurityConfig;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopList;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminCtrl.class)
@ComponentScan(basePackageClasses = {SecurityConfig.class})
class AdminCtrlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @MockBean
    ShopService shopService;

    @MockBean
    GeoService geoService;
    @MockBean
    GeoSearchService geoSearchService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getHome_unauthorized() throws Exception {
        mockMvc.perform(
                get(UrlMapping.ADMIN)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @WithMockUser(authorities = {"BASIC"})
    @Test
    void getHome_forbidden() throws Exception {
        mockMvc.perform(
                get(UrlMapping.ADMIN)
        ).andExpect(
                status().isForbidden()
        );
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void getHome_ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(UrlMapping.ADMIN)
                ).andExpect(status().isOk())
                .andReturn();
        String expected = "Sie haben keine neuen Nachrichten";

        String actual = mvcResult.getResponse().getContentAsString();
        assertThat(actual).isEqualToIgnoringWhitespace(expected);

    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void getBasicAccounts() throws Exception {
        mockMvc.perform(
                        get(UrlMapping.ADMIN + "/accounts")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void uploadFile() throws Exception {
        String json = """
                [
                                  {
                                    "street": "Merkurstraße 24",
                                    "zip": 24943,
                                    "city": "Flensburg"
                                  },
                                  {
                                    "street": "Dieselstraße 9",
                                    "zip": 25813,
                                    "city": "Husum"
                                  }
                ]
                                
                """;
        String name = "en-DE_name";
        Set<Address> addresses = objectMapper.readValue(json, new TypeReference<>() {
        });
        ShopList shopList = new ShopList(name, addresses);
        when(shopService.addAddresses(eq(name), anySet())).thenReturn(shopList);
        MockMultipartFile jsonFile = new MockMultipartFile("mfile", "", "application/json",
                json.getBytes());
        mockMvc.perform(
                multipart(UrlMapping.ADMIN + "/uploadJson")
                        .file(jsonFile)
                        .param("name", name)
                        .with(csrf())
        ).andExpect(status().isOk());
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void uploadFile_bad() throws Exception {
        String json = "[{\nstreet\": \"Merkurstraße 24\"";
        String name = "en-DE_name";
        MockMultipartFile jsonFile = new MockMultipartFile("mfile", "", "application/json",
                json.getBytes());

        mockMvc.perform(
                multipart(UrlMapping.ADMIN + "/uploadJson")
                        .file(jsonFile)
                        .param("name", name)
                        .with(csrf())
        ).andExpect(status().isBadRequest());
    }

}
