package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(UrlMapping.BASIC)
public class BasicCtrl {
    @GetMapping
    public ResponseEntity<String> getPrivateHome() {
        return new ResponseEntity<>("Hi, you are in private home!", HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<Account> getAccount() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth.getPrincipal() instanceof Account account) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/logout")
    public void logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.clearContext();
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(null);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
