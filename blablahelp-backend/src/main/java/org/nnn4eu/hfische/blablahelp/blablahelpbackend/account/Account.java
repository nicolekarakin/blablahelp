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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "account")
public class Account implements UserDetails {
    @Id
    private String id = UUID.randomUUID().toString();
    @NotBlank @Email
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String email;
    @Version
    private Long version;
    @NotBlank @Size(max = 10,min=5)
    private String password;
    @NotBlank @Size(min=1)
    private Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
    @NotBlank
    private String firstname;
    @NotBlank
    private String city;
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

    public Account(String password, String email) {
        this.password = password;
        this.email = email;
        this.authorities = getGranted(Set.of(ERole.BASIC));
    }
    public Account(String password, String email, String firstname,String city) {
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.city = city;
        this.authorities = getGranted(Set.of(ERole.BASIC));
    }
    public Account(String password, String email, String firstname,String city, Set<ERole> roles) {
        this(password, email,firstname,city);

        this.authorities = getGranted(roles);
    }

    public Account(String password, String email, String firstname,String city, Set<ERole> roles, boolean enabled) {
        this(password, email, firstname, city,roles);
        this.enabled = enabled;
    }

    private List<SimpleGrantedAuthority> getGranted(Set<ERole> roles) {
        return roles.stream().map(a -> new SimpleGrantedAuthority(a.name()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id.equals(account.id) && email.equals(account.email) && Objects.equals(version, account.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, version);
    }
}

