package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.SecurityConfig;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan(basePackageClasses = {SecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PublicCtrl.class)
class PublicCtrlIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @MockBean
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Account anna;

    @BeforeAll
    public void init() {
        anna = new Account(
                passwordEncoder.encode("anna1234"),
                "anna@", "anna","München",
                Set.of(ERole.BASIC),
                true
        );
    }


    @Test
    void getHome_ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(UrlMapping.PUBLIC)
                ).andExpect(status().isOk())
                .andReturn();
        String expected = "You are in public home!";

        String actual = mvcResult.getResponse().getContentAsString();
        assertThat(actual).isEqualToIgnoringWhitespace(expected);
    }

    @Test
    void getMe() throws Exception {
        mockMvc.perform(
                get(UrlMapping.PUBLIC + "/me")
                        .with(user(anna))
        ).andExpect(status().isOk())
        ;
    }

    @Test
    void getLogin() throws Exception {
        when(userDetailsService.loadUserByUsername("anna@")).thenReturn(anna);
        MvcResult mvcResult = mockMvc.perform(
                        get(UrlMapping.PUBLIC + "/login")
                                .with(httpBasic("anna@", "anna1234"))
                ).andExpect(status().isOk())
                .andReturn();
        LoginResponse expected = new LoginResponse(anna.getId(), anna.getFirstname(), anna.getEmail(), anna.getCity());
        LoginResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LoginResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

}
