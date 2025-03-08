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
    private final String id;

    @Getter
    private final String email;

    private final String password;

    @Getter
    private final String organizationId;

    @Getter
    private final Boolean mfaEnabled;

    @Getter
    private final String clusterId;


    private final List<GrantedAuthority> grantedAuthorities;

    public CustomUser(List<GrantedAuthority> grantedAuthorities,String id, String email,
                      String organizationId,String password,Boolean mfaEnabled,String clusterId) {

        this.grantedAuthorities = grantedAuthorities;
        this.id=id;
        this.email=email;
        this.organizationId=organizationId;
        this.password=password;
        this.mfaEnabled=mfaEnabled;
        this.clusterId=clusterId;

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
