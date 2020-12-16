package com.ferroeduardo.cartorios_api_frontend.entity;

import com.ferroeduardo.cartorios_api_frontend.entity.roles.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cartorios_api_usuarios")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", length = 60, unique = true)
    private String username;

    @Column(name = "email", length = 60, unique = true)
    private String email;

    @Column(name = "password", length = 60, unique = true)
    private String password;

    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked;

    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired;

    @Column(name = "account_enabled", nullable = false)
    private boolean accountEnabled;

    @Column(name = "user_role", length = 30, nullable = false)
    private String role;

    @Column(name = "api_access", nullable = false)
    private boolean apiAccessible;

    public User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountExpired = false;
        this.accountLocked = false;
        this.credentialsExpired = false;
        this.accountEnabled = true;
        this.role = role.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        String[] roles = role.split(",");
        for (String role : roles) {
            list.add(new SimpleGrantedAuthority(role));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) &&
                email.equals(user.email) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountExpired=" + accountExpired +
                ", accountLocked=" + accountLocked +
                ", credentialsExpired=" + credentialsExpired +
                ", accountEnabled=" + accountEnabled +
                ", role='" + role + '\'' +
                '}';
    }
}
