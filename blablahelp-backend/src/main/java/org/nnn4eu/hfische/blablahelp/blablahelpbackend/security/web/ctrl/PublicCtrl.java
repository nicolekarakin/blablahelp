package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl.model.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(UrlMapping.PUBLIC)
public class PublicCtrl {

    @GetMapping
    public ResponseEntity<String> getPublicHome() {
        return new ResponseEntity<>("You are in public home!", HttpStatus.OK);
    }

    @GetMapping(path = "/login")
    public ResponseEntity<LoginResponse> login() {
        LoginResponse response = getLoginResponse().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<LoginResponse> getMe() {
        return ResponseEntity.of(getLoginResponse());
    }

    private Optional<LoginResponse> getLoginResponse() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (auth.getPrincipal() instanceof Account account) {
            LoginResponse response = new LoginResponse(account.getId(), account.getFirstname(),
                    account.getEmail(), account.getCity());
            return Optional.of(response);
        }
        return Optional.empty();
    }
}
