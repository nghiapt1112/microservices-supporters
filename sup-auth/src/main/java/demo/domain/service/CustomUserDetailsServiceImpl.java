package demo.domain.service;

import demo.domain.User;
import demo.domain.UserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private Map<String, User> userByName = new HashMap();
    {
        userByName.put("read", new User("read", "123", Arrays.asList("READ")));
        userByName.put("write", new User("write", "123", Arrays.asList("READ", "WRITE")));
        userByName.put("user1", new User("john", "123", Arrays.asList("READ", "USER")));
        userByName.put("admin", new User("admin", "123", Arrays.asList("ADMIN")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userByName.get(username);
        if (Objects.isNull(user)) {
            throw new UserException(123, "User Not found");
        }

        org.springframework.security.core.userdetails.User credentialUser = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                user.getAuthorities());
        return credentialUser ;

    }


}
