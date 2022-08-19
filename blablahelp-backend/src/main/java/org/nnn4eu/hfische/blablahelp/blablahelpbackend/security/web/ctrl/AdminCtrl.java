package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMapping.ADMIN)
public class AdminCtrl {
    @GetMapping
    public ResponseEntity<String> getAdminHome() {
        return new ResponseEntity<>("Hi, you are in admin home!", HttpStatus.OK);
    }
}
