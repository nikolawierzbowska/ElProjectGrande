package pl.elgrandeproject.elgrande.user;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.user.dto.UserDto;
import pl.elgrandeproject.elgrande.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

    }

    public List<UserDto> getUsers() {
        return userRepository.findAllBy().stream()
                .map(entity -> userMapper.mapEntityToDto(entity))
                .toList();
    }

    public UserDto getUserById(UUID id) {
        return userRepository.findOneById(id)
                .map(entity -> userMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getUserNotFoundException(id));
    }

    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> userMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getUserNotFoundException(email));
    }

    public void softDeleteUser(UUID id) {
        UserClass user = userRepository.findOneById(id)
                .orElseThrow(() -> getUserNotFoundException(id));

        clearSoftData(user);
        userRepository.save(user);
    }

    private void clearSoftData(UserClass user) {
        int firstNameLength = user.getFirstName().length();
        user.setFirstName("*".repeat(firstNameLength));

        int lastNameLength = user.getLastName().length();
        user.setLastName("*".repeat(lastNameLength));

        int atCharPos = user.getEmail().indexOf('@');
        String randomPref = "deleted-" + UUID.randomUUID();
        user.setEmail(randomPref + user.getEmail().substring(atCharPos));

        int passwordLength = user.getPassword().length();
        user.setPassword("*".repeat(passwordLength));

        int repeatedPasswordLength = user.getRepeatedPassword().length();
        user.setRepeatedPassword("*".repeat(repeatedPasswordLength));
    }

    private UserNotFoundException getUserNotFoundException(String email) {
        return new UserNotFoundException("User with this email " + email + " not exist");
    }

    private UserNotFoundException getUserNotFoundException(UUID id) {
        return new UserNotFoundException("User with this " + id + "  not exist");
    }

}
