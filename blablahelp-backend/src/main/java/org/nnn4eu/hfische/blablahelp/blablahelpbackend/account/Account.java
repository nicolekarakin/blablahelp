package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "account")
public class Account implements UserDetails {
    @Id
    private String id = UUID.randomUUID().toString();
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String username;//email
    @Version
    private Long version;
    private String password;
    private Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
    private String firstname;
    private boolean enabled = false;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant createdAt;
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant modifiedAt;

    public void addAuthority(ERole role) {
        authorities.add(new SimpleGrantedAuthority(role.name()));
    }

    public void removeAuthority(ERole role) {
        authorities.remove(new SimpleGrantedAuthority(role.name()));
    }

    public Account(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public Account(String password, String username, String firstname, Collection<SimpleGrantedAuthority> authorities) {
        this(password, username);
        this.firstname = firstname;
        this.authorities = authorities;
    }

    public Account(String password, String username, String firstname, Collection<SimpleGrantedAuthority> authorities, boolean enabled) {
        this(password, username, firstname, authorities);
        this.enabled = enabled;
    }

    public Account enable() {
        this.enabled = true;
        return this;
    }

    public Account disable() {
        this.enabled = false;
        return this;
    }

    public Account lock() {
        this.accountNonLocked = true;
        return this;
    }

    public Account unlock() {
        this.accountNonLocked = false;
        return this;
    }

    public Account expireCredentials() {
        this.credentialsNonExpired = false;
        return this;
    }

    public Account persistCredentials() {
        this.credentialsNonExpired = true;
        return this;
    }

    public Account expireAccount() {
        this.accountNonExpired = false;
        return this;
    }

    public Account persistAccount() {
        this.accountNonExpired = true;
        return this;
    }
}

