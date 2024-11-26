package com.credaegis.backend.configuration.security.principal;

import com.credaegis.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUser implements UserDetails {

    private String email;
    private String password;
    private List<GrantedAuthority> grantedAuthorities;

    public CustomUser(String email, String password, List<GrantedAuthority> grantedAuthorities){
        this.email = email;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
