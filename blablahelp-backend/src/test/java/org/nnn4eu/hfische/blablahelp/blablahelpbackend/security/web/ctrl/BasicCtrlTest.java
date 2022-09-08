package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoSearchService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.SecurityConfig;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BasicCtrl.class)
@ComponentScan(basePackageClasses = SecurityConfig.class)
class BasicCtrlTest {
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

    @Test
    void getHome_unauthorized() throws Exception {
        mockMvc.perform(
                get(UrlMapping.BASIC)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @WithMockUser(authorities = {"BASIC"})
    @Test
    void getHome_ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(UrlMapping.BASIC)
                ).andExpect(status().isOk())
                .andReturn();
        String expected = "You are in private home!";

        String actual = mvcResult.getResponse().getContentAsString();
        assertThat(actual).isEqualToIgnoringWhitespace(expected);
    }

    @WithMockUser(authorities = {"BASIC"})
    @Test
    void logout() throws Exception {
        mockMvc.perform(
                get(UrlMapping.BASIC + "/logout")
        ).andExpect(status().isOk());

    }

    @Test
    void logout_unauthorized() throws Exception {
        mockMvc.perform(
                get(UrlMapping.BASIC + "/logout")
        ).andExpect(status().isUnauthorized());

    }
}
