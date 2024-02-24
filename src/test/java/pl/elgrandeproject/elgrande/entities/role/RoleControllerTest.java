package pl.elgrandeproject.elgrande.entities.role;


import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.elgrandeproject.elgrande.config.SecurityConfiguration;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.UpdateRoleDto;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.elgrandeproject.elgrande.config.SecurityConfiguration.ADMIN;

@WebMvcTest(controllers = RoleController.class)
@Import(SecurityConfiguration.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoleService roleService;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadAllRolesDto() throws Exception {
        //given:
        Role role1 = new Role("test-name1");
        Role role2 = new Role("test-name2");

        UserClass userClass1 = Instancio.create(UserClass.class);
        UserClass userClass2 = Instancio.create(UserClass.class);

        RoleDto.UserNameId user1 = new RoleDto.UserNameId(userClass1.getId(), userClass1.getEmail());
        RoleDto.UserNameId user2 = new RoleDto.UserNameId(userClass2.getId(), userClass2.getEmail());
        List<RoleDto.UserNameId> usersList = List.of(user1, user2);

        RoleDto roleDto1 = new RoleDto(role1.getId(), role1.getName(), null);
        RoleDto roleDto2 = new RoleDto(role2.getId(), role2.getName(), usersList);

        List<RoleDto> rolesListDto = List.of(roleDto1, roleDto2);

        Mockito.when(roleService.getAllRoles())
                .thenReturn(rolesListDto);

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/roles"));

        //then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(rolesListDto.get(0).id().toString()))
                .andExpect(jsonPath("$[0].name").value(rolesListDto.get(0).name()))
                .andExpect(jsonPath("$[0].users").value(rolesListDto.get(0).users()))
                .andExpect(jsonPath("$[1].id").value(rolesListDto.get(1).id().toString()))
                .andExpect(jsonPath("$[1].name").value(rolesListDto.get(1).name()))
                .andExpect(jsonPath("$[1].users[0].id").value(rolesListDto.get(1).users().get(0).id().toString()))
                .andExpect(jsonPath("$[1].users[0].email").value(rolesListDto.get(1).users().get(0).email()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadRoleById() throws Exception {
        //given:
        Role role1 = new Role("test-name1");
        RoleDto roleDto1 = new RoleDto(role1.getId(), role1.getName(), null);

        Mockito.when(roleService.getRoleById(role1.getId()))
                .thenReturn(roleDto1);

        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/admin/roles/id/" + role1.getId()));

        //then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleDto1.id().toString()))
                .andExpect(jsonPath("$.name").value(roleDto1.name()))
                .andExpect(jsonPath("$.users").value(roleDto1.users()));
    }

    @Test
    void shouldReturnUnauthorizedWhenGetRoleById() throws Exception {
        //given:
        Role role1 = new Role("test-name1");
        RoleDto roleDto1 = new RoleDto(role1.getId(), role1.getName(), null);

        Mockito.when(roleService.getRoleById(role1.getId()))
                .thenReturn(roleDto1);

        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/admin/roles/id/" + role1.getId()));
        //then:
        response.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404NotFoundRoleById() throws Exception {
        //given:
        UUID roleId = UUID.randomUUID();

        Mockito.when(roleService.getRoleById(roleId))
                .thenThrow(new RoleNotFoundException("Rola z takim id:  " + roleId + " nie istnieje"));
        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/admin/roles/id/" + roleId));

        //then:
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadRoleByName() throws Exception {
        //given:
        Role role1 = new Role("test-name1");
        RoleDto roleDto1 = new RoleDto(role1.getId(), role1.getName(), null);

        Mockito.when(roleService.getRoleByName(role1.getName()))
                .thenReturn(roleDto1);

        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/admin/roles/" + role1.getName()));

        //then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleDto1.id().toString()))
                .andExpect(jsonPath("$.name").value(roleDto1.name()))
                .andExpect(jsonPath("$.users").value(roleDto1.users()));
    }


    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404NotFoundRoleByName() throws Exception {
        //given:
        String name = "test-name1";

        Mockito.when(roleService.getRoleByName(name))
                .thenThrow(new RoleNotFoundException("Rola z taką nazwą: " + name + " nie istnieje"));
        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/admin/roles/" + name));

        //then:
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldCreateRole() throws Exception {
        //given:
        NewRoleDto newRoleDto = new NewRoleDto("name-role");
        Role role1 = new Role(newRoleDto.getName());
        RoleDto roleDto = new RoleDto(role1.getId(), role1.getName(), null);

        Mockito.when(roleService.saveNewRole(newRoleDto))
                .thenReturn(roleDto);

        //when:
        ResultActions response = mockMvc.perform(
                post("/api/v1/admin/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "name-role"
                                }
                                """)
        );
        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldChangeNameRole() throws Exception {
        //given:
        UpdateRoleDto newRoleDto = new UpdateRoleDto("newName");
        Role role = new Role(newRoleDto.getName());
        UserClass userClass = Instancio.create(UserClass.class);

        Mockito.when(roleRepository.findByName(role.getName()))
                .thenReturn(Optional.of(role));

        Mockito.when(userRepository.findOneById(userClass.getId()))
                .thenReturn(Optional.of(userClass));

        roleService.changeRoleToUser(role.getId(), userClass.getId(), newRoleDto);


        //when:
        ResultActions response = mockMvc.perform(
                patch("/api/v1/admin/roles/" + role.getId() + "/users/" + userClass.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "newName"
                                }
                                """)
        );
        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn409WhenInsertEmptyRoleNameWhenChangeRoleToUser() throws Exception {
        //given:
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        //when:
        ResultActions response = mockMvc.perform(
                patch("/api/v1/admin/roles/" + roleId + "/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": ""
                                }
                                """)
        );
        //then:
        response.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldDeleteRole() throws Exception {
        //given:
        UUID role1 = UUID.randomUUID();
        //when:
        ResultActions response = mockMvc.perform(
                delete("/api/v1/admin/roles/" + role1));

        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldUpdateRole() throws Exception {
        //given:
        NewRoleDto newRoleDto = new NewRoleDto("name-role");
        Role role1 = new Role(newRoleDto.getName());
        roleService.updateRoleById(role1.getId(), newRoleDto);

        //when:
        ResultActions response = mockMvc.perform(
                patch("/api/v1/admin/roles/" + role1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "name-role"
                                }
                                """)
        );
        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn409WhenUpdateNameRoleName() throws Exception {
        //given:
        UUID role1 = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                patch("/api/v1/admin/roles/" + role1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": ""
                                }
                                """)
        );
        //then:
        response.andExpect(status().isBadRequest());
    }
}