package demo.application;

import demo.domain.User;
import demo.domain.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/")
    public User create(User user) {
        return this.userService.createOne(user);
    }

}
