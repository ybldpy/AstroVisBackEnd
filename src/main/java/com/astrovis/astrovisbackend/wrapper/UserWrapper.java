package com.astrovis.astrovisbackend.wrapper;

import com.astrovis.astrovisbackend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserWrapper extends User implements UserDetails {

    private User user;

    public UserWrapper(User user){
        this.user = user;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }


    @Override
    public Integer getUid() {
        return user.getUid();
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }


    public User getUser() {
        return user;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {

        return user.equals(o);
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }
}
