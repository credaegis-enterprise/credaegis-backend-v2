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
    @Setter
    private String  email;

    @Setter
    private String password;

    @Getter
    @Setter
    private String organizationId;

    @Getter
    @Setter
    private String userId;
    private List<GrantedAuthority> grantedAuthorities;

    public CustomUser(String email, String password, String organizationId, String userId,
                      List<GrantedAuthority> grantedAuthorities){

        this.email = email;
        this.password = password;
        this.organizationId = organizationId;
        this.userId = userId;
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
