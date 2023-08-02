package owt.training.opa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import owt.training.opa.model.User;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getAllUsers() {
        return List.of(new User("admin-user", "Admin User", 1),
                new User("read-user", "Read User", 2),
                new User("write-user", "Write User", 3));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createUser(@RequestBody User user) {
        log.info("Added new User {}", user);
    }

    @PutMapping
    public User updateUserDetails(@RequestBody User user) {
        log.info("Update User details {}", user);
        return user;
    }
}
