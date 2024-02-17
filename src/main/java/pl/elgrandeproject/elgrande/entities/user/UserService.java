package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getUsers() {
        return userRepository.findAllBy().stream()
                .map(entity -> userMapper.mapEntityToDto(entity))
                .toList();
    }

    public UserDto getUserById(UUID userId) {
        return userRepository.findOneById(userId)
                .map(entity -> userMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getUserNotFoundException(userId));
    }

    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> userMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getUserWithThisEmailNotFoundException(email));
    }

    public void softDeleteUser(UUID userId) {
        UserClass user = userRepository.findOneById(userId)
                .orElseThrow(() -> getUserNotFoundException(userId));
        clearSoftData(user);
        user.clearAssignRole();
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

    public static UserNotFoundException getUserWithThisEmailNotFoundException(String email) {
        return new UserNotFoundException("User z tym email " + email + " nie istnieje");
    }

    public static UserNotFoundException getUserNotFoundException(UUID userId) {
        return new UserNotFoundException("User z tym id: " + userId + " nie  istnieje");
    }
}
