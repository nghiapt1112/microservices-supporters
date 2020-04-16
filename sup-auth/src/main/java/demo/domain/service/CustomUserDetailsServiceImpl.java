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
        userByName.put("test_common", new User("test_common", "123", Arrays.asList("READ")));
        userByName.put("test1", new User("test1", "123", Arrays.asList("READ", "WRITE")));
        userByName.put("test2", new User("test2", "123", Arrays.asList("READ", "USER")));
        userByName.put("admin", new User("admin", "123", Arrays.asList("ADMIN")));
        userByName.put("test3", new User("test3", "123", Arrays.asList("ROLE3")));
        userByName.put("test4", new User("test4", "123", Arrays.asList("ROLE3", "ROLE4")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userByName.get(username);
        if (Objects.isNull(user)) {
            throw new UserException("User Not found");
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
