package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Entity.UserEntity;
import Tingeso.Web_mono.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/getUsers")
    public List<UserEntity> getUsers() {
        return userService.getAllUsers();
    }
}