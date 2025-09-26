package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Entity.UserEntity;
import Tingeso.Web_mono.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
