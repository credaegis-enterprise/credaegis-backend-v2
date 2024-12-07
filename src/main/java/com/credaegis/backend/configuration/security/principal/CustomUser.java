package com.credaegis.backend.configuration.security.principal;

import com.credaegis.backend.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUser implements UserDetails {



    @Getter
    private final User user;

    private final List<GrantedAuthority> grantedAuthorities;

    public CustomUser(List<GrantedAuthority> grantedAuthorities,User user) {

        this.grantedAuthorities = grantedAuthorities;
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


}
