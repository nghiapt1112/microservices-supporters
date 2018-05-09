package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class AuthserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthserverApplication.class, args);
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

}
