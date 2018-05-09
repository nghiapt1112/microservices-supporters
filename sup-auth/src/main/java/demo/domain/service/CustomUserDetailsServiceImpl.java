package demo.service;

import demo.domain.Roles;
import demo.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();

        List<Roles> roles = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                this.generateGrantedAuthorities(roles));
    }

    public Set<GrantedAuthority> generateGrantedAuthorities(Collection<Roles> roles) {
        Set<GrantedAuthority> grantedAuthorities = roles
                .stream().map(r -> new SimpleGrantedAuthority(this.parseToOAuthRole(r)))
                .collect(Collectors.toSet());
        return grantedAuthorities;
    }

    private String parseToOAuthRole(Roles r) {
        return ROLE_PREFIX.concat(r.getRoleNameUpperCase());
    }
}
