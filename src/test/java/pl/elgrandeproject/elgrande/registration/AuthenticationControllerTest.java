package pl.elgrandeproject.elgrande.registration;


import org.hamcrest.Matchers;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import pl.elgrandeproject.elgrande.config.SecurityConfiguration;
import pl.elgrandeproject.elgrande.entities.role.RoleRepository;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.security.jwt.JwtAuthenticationResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuthenticationController.class)
@Import(SecurityConfiguration.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;


    @Test
    void shouldReturnNewUserDto() throws Exception {
        //given:
        NewUserDto newUserDto = new NewUserDto("test-firstName", "test-lastName",
                "test-email@gmail.com", "test-password", "test-password");

        UUID id = UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294");
        UUID idRole = UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294");
        UserDto.UserRole userRole = new UserDto.UserRole(idRole, "USER");
        UserDto userDto = new UserDto(id, "test-firstName", "test-lastName",
                "test-email@gmail.com", "test-password", "test-password",
                List.of(userRole));

        Mockito.when(authenticationService.registerUser(newUserDto))
                .thenReturn(userDto);

        //when:

        ResultActions response = mockMvc.perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "test-firstName",
                            "lastName": "test-lastName",
                            "email": "test-email@gmail.com",
                            "password": "test-password",
                            "repeatedPassword": "test-password"
                        }
                        """));
        //then:
        response
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenInputDataIsInValid() throws Exception {
        //when:
        ResultActions response = mockMvc.perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "",
                            "lastName": "",
                            "email": "test-email@gmail.com",
                            "password": "test-password",
                            "repeatedPassword": "test-password"
                        }
                        """));
        //then:
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value(Matchers.containsString(
                        "The field can not be empty."
                )));
    }

    @Test
    void shouldReturn409WhenPasswordsNotMatch() throws Exception {
        //given:
        NewUserDto newUserDto = new NewUserDto("test-firstName", "test-lastName",
                "test-email@gmail.com", "test-password", "test-password2");
        UUID id = UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294");
        UserDto userDto = new UserDto(id, "test-firstName", "test-lastName",
                "test-email@gmail.com", "test-password", "test-password2",
                List.of());
        Mockito.when(authenticationService.registerUser(newUserDto))
                .thenReturn(userDto);

        //when:
        ResultActions response = mockMvc.perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "test-firstName",
                            "lastName": "test-lastName",
                            "email": "test-email@gmail.com",
                            "password": "test-password",
                            "repeatedPassword": "test-password2"
                        }
                        """));
        //then:
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value(Matchers.containsString("Passwords do not match! ")));
    }

    @Test
    void shouldReturn409WhenEmailExist() throws Exception {
        //given:
        String email = "test-email@gmail.com";
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(Instancio.create(UserClass.class)));

        //when:
        ResultActions response = mockMvc.perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "test-firstName",
                            "lastName": "test-lastName",
                            "email": "test-email@gmail.com",
                            "password": "test-password",
                            "repeatedPassword": "test-password"
                        }
                        """));
        //then:
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.info").value(Matchers.containsString(email + " is already in use")));
    }

    @Test
    void shouldReturn200WhenUserLogin() throws Exception {
        //given:
        LoginUser loginUser = new LoginUser("test-email@gmail.com", "test-password");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuaWtvbGFAZ21haWwuY29tIiwiaWF0IjoxNzA4MjY5ODYzLCJleHAiOjE3MDgyNzEzMDN9.1O8AFHt1oOGnp-_4SeSo-FtyiElgnI9JsvaPVO-9evw";
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        Mockito.when(authenticationService.login(loginUser))
                .thenReturn(jwtAuthenticationResponse);

        //when:
        ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "test-email@gmail.com",
                            "password": "test-password"
                        }
                        """));
        //then:
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }



}