package pl.elgrandeproject.elgrande.entities.user;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.elgrandeproject.elgrande.config.SecurityConfiguration;
import pl.elgrandeproject.elgrande.entities.role.Role;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.elgrandeproject.elgrande.config.SecurityConfiguration.ADMIN;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository repository;

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404WhenUserIsNotFound() throws Exception {
        //given:
        UUID userId = UUID.randomUUID();

        Mockito.when(userService.getUserById(userId))
                .thenThrow(new UserNotFoundException("User z tym id: " + userId + " nie  istnieje"));

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/users/" + userId));

        //then:
        response.andExpect(status().isNotFound());
        response.andExpect(jsonPath("$.info")
                .value("User z tym id: " + userId + " nie  istnieje"));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturnUserByIdJson() throws Exception {
        //given:
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UserDto.UserRole role = new UserDto.UserRole(roleId, "admin");

        UserDto userDto = new UserDto(userId, "Anna", "Nowak", "ana@gmail.com",
                "123456", "123456", List.of(role));
        Mockito.when(userService.getUserById(userId))
                .thenReturn(userDto);

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/users/" + userId));

        //then:
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id().toString()))
                .andExpect(jsonPath("$.firstName").value(userDto.firstName().toString()))
                .andExpect(jsonPath("$.lastName").value(userDto.lastName().toString()))
                .andExpect(jsonPath("$.email").value(userDto.email().toString()))
                .andExpect(jsonPath("$.userRoles.size()").value(1))
                .andExpect(jsonPath("$.userRoles[0].id").value(userDto.userRoles().get(0).id().toString()))
                .andExpect(jsonPath("$.userRoles[0].name").value(userDto.userRoles().get(0).name()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404WhenReadUserByEmailFromDb() throws Exception {
        //given:
        String email = "anna3@test.com";
        Mockito.when(userService.getUserByEmail(email))
                .thenThrow(new UserNotFoundException("User z tym email " + email + " nie istnieje"));

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/users/by-" + email));

        //then:
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("User z tym email " + email + " nie istnieje"));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturnUserByEmailJson() throws Exception {
        //given:
        String email = "anna@test.com";
        UUID userId = UUID.randomUUID();
        UserDto userDto = new UserDto(userId, "Anna", "Nowak", "anna@test.com",
                "123456", "123456", null);

        Mockito.when(userService.getUserByEmail(email))
                .thenReturn(userDto);

        // when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/users/by-" + email));

        //then:
        response.andExpect(jsonPath("$.id").value(userDto.id().toString()));
        response.andExpect(jsonPath("$.firstName").value(userDto.firstName()));
        response.andExpect(jsonPath("$.lastName").value(userDto.lastName()));
        response.andExpect(jsonPath("$.email").value(userDto.email()));
        response.andExpect(jsonPath("$.userRoles").value(userDto.userRoles()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadUsersListFromDb() throws Exception {
        //given:
        UUID userId = UUID.randomUUID();
        UserDto userDto1 = new UserDto(userId, "Anna", "Nowak", "anna@test.com",
                "123456", "123456", null);


        UUID userId2 = UUID.randomUUID();

        Role role1 = new Role("admin");
        Role role2 = new Role("user");
        UserDto.UserRole uRole1 = new UserDto.UserRole(role1.getId(), role1.getName());
        UserDto.UserRole uRole2 = new UserDto.UserRole(role2.getId(), role2.getName());

        UserDto userDto2 = new UserDto(userId2, "Anna", "Nowak", "anna@test.com",
                "123456", "123456", List.of(uRole1, uRole2));

        List<UserDto> usersDto = List.of(userDto1, userDto2);
        Mockito.when(userService.getUsers())
                .thenReturn(usersDto);
        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/users"));

        //then:
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(usersDto.get(0).id().toString()))
                .andExpect(jsonPath("$[0].firstName").value(usersDto.get(0).firstName()))
                .andExpect(jsonPath("$[0].lastName").value(usersDto.get(0).lastName()))
                .andExpect(jsonPath("$[0].email").value(usersDto.get(0).email()))
                .andExpect(jsonPath("$[0].userRoles").value(usersDto.get(0).userRoles()))

                .andExpect(jsonPath("$[1].id").value(usersDto.get(1).id().toString()))
                .andExpect(jsonPath("$[1].firstName").value(usersDto.get(1).firstName()))
                .andExpect(jsonPath("$[1].lastName").value(usersDto.get(1).lastName()))
                .andExpect(jsonPath("$[1].email").value(usersDto.get(1).email()))
                .andExpect(jsonPath("$[1].userRoles.size()").value(2))

                .andExpect(jsonPath("$[1].userRoles[0].id").value(usersDto.get(1).userRoles().get(0).id().toString()))
                .andExpect(jsonPath("$[1].userRoles[0].name").value(usersDto.get(1).userRoles().get(0).name()))

                .andExpect(jsonPath("$[1].userRoles[1].id").value(usersDto.get(1).userRoles().get(1).id().toString()))
                .andExpect(jsonPath("$[1].userRoles[1].name").value(usersDto.get(1).userRoles().get(1).name()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldDeleteUser() throws Exception {
        //given:
        UUID userId = UUID.randomUUID();
//        UserDto userDto1 = new UserDto(userId, "Anna", "Nowak", "anna@test.com",
//                "123456", "123456", null);

        //when:
        ResultActions response = mockMvc.perform(delete("/api/v1/admin/users/" + userId));
        //then:
        response
                .andExpect(status().isOk());
    }
}
