package pl.elgrandeproject.elgrande.entities.user;

import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import pl.elgrandeproject.elgrande.config.SecurityConfiguration;
import pl.elgrandeproject.elgrande.entities.role.Role;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.entities.user.exception.ForbiddenUserAccessException;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.registration.Principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.elgrandeproject.elgrande.config.SecurityConfiguration.USER;

@ExtendWith(MockitoExtension.class)
@Import(SecurityConfiguration.class)
class UserServiceTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserMapper userMapper = Mockito.mock(UserMapper.class);

    private final UserService testUserService = new UserService(userRepository, userMapper);

    @Test
    void shouldReturnAllUsersDto() {
        //given:
        UserClass user1 = new UserClass("name1", "lastname1", "email1", "password1",
                "rPassword1");
        UserClass user2 = new UserClass("name2", "lastname2", "email2", "password2",
                "rPassword2");
        List<UserClass> usersList = List.of(user1, user2);

        Role role = new Role("role");
        user1.addRole(role);

        UserDto.UserRole userRole = new UserDto.UserRole(role.getId(), role.getName());
        List<UserDto.UserRole> userRoles = List.of(userRole);

        UserDto userDto1 = new UserDto(user1.getId(), user1.getFirstName(), user1.getLastName(), user1.getEmail(),
                user1.getPassword(), user1.getRepeatedPassword(), userRoles);

        UserDto userDto2 = new UserDto(user2.getId(), user2.getFirstName(), user2.getLastName(), user2.getEmail(),
                user2.getPassword(), user2.getRepeatedPassword(), null);

        List<UserDto> usersDtoList = List.of(userDto1, userDto2);

        Mockito.when(userRepository.findAllBy())
                .thenReturn(usersList);

        Mockito.when(userMapper.mapEntityToDto(usersList.get(0)))
                .thenReturn(usersDtoList.get(0));
        Mockito.when(userMapper.mapEntityToDto(usersList.get(1)))
                .thenReturn(usersDtoList.get(1));

        //when:
        List<UserDto> actualList = testUserService.getUsers();

        //then:
        Assertions.assertThat(actualList.size()).isEqualTo(2);
        Assertions.assertThat(actualList.contains(userDto1)).isTrue();
        Assertions.assertThat(actualList.contains(userDto2)).isTrue();
        Assertions.assertThat(actualList).isEqualTo(usersDtoList);
        Assertions.assertThat(actualList.get(0).userRoles().size()).isEqualTo(1);
        Assertions.assertThat(actualList.get(1).userRoles()).isNull();
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenGetUserById() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(userRepository.findOneById(id))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testUserService.getUserById(id));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("User z tym id: " + id + " nie  istnieje");
    }

    @Test
    void shouldReturnUserDtoById() {
        //given:
        UserClass user1 = new UserClass("name1", "lastname1", "email1", "password1",
                "rPassword1");
        UserDto userDto = new UserDto(user1.getId(), user1.getFirstName(), user1.getLastName(), user1.getEmail(),
                user1.getPassword(), user1.getRepeatedPassword(), null);

        Mockito.when(userRepository.findOneById(user1.getId()))
                .thenReturn(Optional.of(user1));

        Mockito.when(userMapper.mapEntityToDto(user1))
                .thenReturn(userDto);

        //when:
        UserDto actual = testUserService.getUserById(user1.getId());

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(userDto.id());
        Assertions.assertThat(actual.email()).isEqualTo(userDto.email());
        Assertions.assertThat(actual.password()).isEqualTo(userDto.password());
        Assertions.assertThat(actual.userRoles()).isNull();
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenGetUserByEmail() {
        //given:
        String email = "email";
        UserDetails userDetails = new User("anna@test.com", "password", new ArrayList<>());


        Principal principal = new Principal();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testUserService.getUserByEmail(email, principal));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(ForbiddenUserAccessException.class);
        Assertions.assertThat(throwable).hasMessage("Brak uprawnień");
    }

    @Test
    @WithMockUser(roles = USER)
    void shouldReturnUserDtoByEmail() {
        //given:
        UserClass user = new UserClass("name1", "lastname1", "anna@test.com", "password",
                "rPassword1");
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getRepeatedPassword(), null);

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userMapper.mapEntityToDto(user))
                .thenReturn(userDto);
        UserDetails userDetails = new User("anna@test.com", "password", new ArrayList<>());
        Principal principal = new Principal();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //when:
        UserDto actual = testUserService.getUserByEmail(user.getEmail(), principal);

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(userDto.id());
        Assertions.assertThat(actual.email()).isEqualTo(userDto.email());
        Assertions.assertThat(actual.password()).isEqualTo(userDto.password());
        Assertions.assertThat(actual.userRoles()).isNull();
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenSoftDeleteUser() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(userRepository.findOneById(id))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testUserService.softDeleteUser(id));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("User z tym id: " + id + " nie  istnieje");
    }

    @Captor
    private ArgumentCaptor<UserClass> userArgumentCaptor;

    @Test
    void testSoftDeleteUser() {
        //given:
        UserClass user = Instancio
                .of(UserClass.class)
                .generate(Select.field(UserClass::getFirstName),
                        generators -> generators.text().pattern("name"))
                .generate(Select.field(UserClass::getLastName),
                        generators -> generators.text().pattern("lastName"))
                .generate(Select.field(UserClass::getEmail),
                        generators -> generators.text().pattern("email@gmail.com"))
                .generate(Select.field(UserClass::getPassword),
                        generators -> generators.text().pattern("password"))
                .generate(Select.field(UserClass::getRepeatedPassword),
                        generators -> generators.text().pattern("repeatedPassword"))
                .generate(Select.field(UserClass::getRoles),
                        generators -> generators.collection().size(1))
                .create();

        Mockito.when(userRepository.findOneById(user.getId()))
                .thenReturn(Optional.of(user));
        //when:
        testUserService.softDeleteUser(user.getId());

        //then:
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        Assertions.assertThat(userArgumentCaptor.getValue().getId()).isEqualTo(user.getId());
        Assertions.assertThat(userArgumentCaptor.getValue().getFirstName()).isEqualTo("****");
        Assertions.assertThat(userArgumentCaptor.getValue().getLastName()).isEqualTo("********");
        Assertions.assertThat(userArgumentCaptor.getValue().getEmail()).startsWith("deleted");
    }
}