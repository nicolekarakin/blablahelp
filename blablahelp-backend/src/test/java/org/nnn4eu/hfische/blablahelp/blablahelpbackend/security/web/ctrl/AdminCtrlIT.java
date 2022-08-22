package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminCtrl.class)
@ComponentScan(basePackageClasses = SecurityConfig.class)
class AdminCtrlIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

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
        String expected = "You are in admin home!";

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

}