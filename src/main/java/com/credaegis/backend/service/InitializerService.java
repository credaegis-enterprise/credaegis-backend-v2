package com.credaegis.backend.service;


import com.credaegis.backend.entity.Organization;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.OrganizationRepository;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InitializerService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;


    public void  addUserService(){

        Organization organization = new Organization();
        organization.setId("1");
        organization.setName("Saintgits college of Engineering");
        organization.setAddress("kottayam");
        organization.setPincode("688521");
        organizationRepository.save(organization);



        User user = new User();
        user.setId("1");
        user.setUsername("saintgits");
        user.setEmail("sgce@saintgits.org");
        user.setPassword(passwordEncoder.encode("sgce"));
        user.setOrganization(organization);
        userRepository.save(user);

        Role role = new Role();
        role.setId("1");
        role.setRole("ROLE_ADMIN");
        role.setAuthority("ADMIN");
        role.setUser(user);
        roleRepository.save(role);
        userRepository.save(user);

    }
}
