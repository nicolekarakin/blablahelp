package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.ADMIN)
public class AdminCtrl {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<String> getAdminHome() {
        return new ResponseEntity<>("You are in admin home!", HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getBasicAccounts() {
        return ResponseEntity.ok(accountService.getBasicAccounts(Set.of(ERole.BASIC)));
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
