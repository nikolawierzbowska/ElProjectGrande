package pl.elgrandeproject.elgrande.entities.role;


import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.UpdateRoleDto;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RoleMapper roleMapper = Mockito.mock(RoleMapper.class);
    private final RoleService testRoleService = new RoleService(roleRepository, userRepository, roleMapper);

    @Test
    void shouldReturnAllRolesDto() {
        //given:
        Role role1 = new Role("role1");
        Role role2 = new Role("role2");
        List<Role> roleList = List.of(role1, role2);

        UserClass userClass = new UserClass("name", "lastname", "email", "password",
                "rPassword");
        role1.assignUser(userClass);
        List<RoleDto.UserNameId> roleUserList = new ArrayList<>();
        roleUserList.add(new RoleDto.UserNameId(userClass.getId(), userClass.getEmail()));

        RoleDto roleDto1 = new RoleDto(role1.getId(), "roleDto1", roleUserList);
        RoleDto roleDto2 = new RoleDto(role1.getId(), "roleDto2", null);
        List<RoleDto> roleDtoList = List.of(roleDto1, roleDto2);

        Mockito.when(roleRepository.findAllBy())
                .thenReturn(roleList);

        Mockito.when(roleMapper.mapEntityToDto(roleList.get(0)))
                .thenReturn(roleDtoList.get(0));
        Mockito.when(roleMapper.mapEntityToDto(roleList.get(1)))
                .thenReturn(roleDtoList.get(1));

        //when:
        List<RoleDto> actualList = testRoleService.getAllRoles();

        //then:
        Assertions.assertThat(actualList.size()).isEqualTo(2);
        Assertions.assertThat(actualList.contains(roleDto1)).isTrue();
        Assertions.assertThat(actualList.contains(roleDto2)).isTrue();
        Assertions.assertThat(actualList).isEqualTo(roleDtoList);
        Assertions.assertThat(actualList.get(0).users().size()).isEqualTo(1);
        Assertions.assertThat(actualList.get(1).users()).isNull();
    }

    @Test
    void shouldThrowRoleDtoByIdNotFoundException() {
        //given:
        Role role = new Role("role1");
        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.getRoleById(role.getId()));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Rola z takim id:  " + role.getId() + " nie istnieje");
    }

    @Test
    void shouldReturnRoleDtoById() {
        //given:
        Role role = new Role("role1");
        RoleDto roleDto = new RoleDto(role.getId(), role.getName(), null);

        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.of(role));

        Mockito.when(roleMapper.mapEntityToDto(role))
                .thenReturn(roleDto);

        //when:
        RoleDto actual = testRoleService.getRoleById(role.getId());

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(roleDto.id());
        Assertions.assertThat(actual.name()).isEqualTo(roleDto.name());
        Assertions.assertThat(actual.users()).isNull();
    }

    @Test
    void shouldThrowRoleDtoByNameNotExist() {
        //given:
        String name = "name";

        Mockito.when(roleRepository.findByName(name))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.getRoleByName(name));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Rola z taką nazwą: " + name + " nie istnieje");
    }

    @Test
    void shouldReturnRoleDtoByName() {
        //given:
        Role role = new Role("role1");
        RoleDto roleDto = new RoleDto(role.getId(), "role1", null);

        Mockito.when(roleRepository.findByName(role.getName().toUpperCase()))
                .thenReturn(Optional.of(role));

        Mockito.when(roleMapper.mapEntityToDto(role))
                .thenReturn(roleDto);

        //when:
        RoleDto actual = testRoleService.getRoleByName(role.getName());

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(roleDto.id());
        Assertions.assertThat(actual.name()).isEqualTo("role1");
        Assertions.assertThat(actual.users()).isNull();
    }

    @Test
    void shouldNotSaveNewRoleWhenExistThisName() {
        //given:
        Role role = new Role("role1");
        NewRoleDto newRoleDto = new NewRoleDto("role1");

        Mockito.when(roleRepository.findByName(newRoleDto.getName().toUpperCase()))
                .thenReturn(Optional.of(role));

        //when:
        RoleDto actual = testRoleService.saveNewRole(newRoleDto);

        //then:
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void shouldReturnRoleDto() {
        //given:
        NewRoleDto newRoleDto = new NewRoleDto("new-role".toUpperCase());
        Role role = new Role(newRoleDto.getName());
        RoleDto roleDto = new RoleDto(role.getId(), role.getName(), null);

        Mockito.when(roleRepository.save(role))
                .thenReturn(role);
        Mockito.when(roleMapper.mapNewRoleDtoToEntity(newRoleDto))
                .thenReturn(role);

        Mockito.when(roleMapper.mapEntityToDto(role))
                .thenReturn(roleDto);

        //when:
        RoleDto actual = testRoleService.saveNewRole(newRoleDto);

        //then:
        Assertions.assertThat(actual.id()).isEqualTo(roleDto.id());
        Assertions.assertThat(actual.name()).isEqualTo(roleDto.name());
        Assertions.assertThat(actual.users()).isNull();
    }

    @Test
    void shouldThrowRoleNotFoundException() {
        //given:
        Role role = new Role("role1");
        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.findRoleById(role.getId()));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Rola z takim id:  " + role.getId() + " nie istnieje");
    }

    @Test
    void shouldReturnRoleById() {
        //given:
        Role role = new Role("role1");
        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.of(role));

        //when:
        Role actual = testRoleService.findRoleById(role.getId());

        //then:
        Assertions.assertThat(actual.getName()).isEqualTo(role.getName());
        Assertions.assertThat(actual.getUsers()).isEmpty();
    }

    @Test
    void shouldThrowUserByIdNotFoundException() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(userRepository.findOneById(id))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.findUserById(id));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("User z tym id: " + id + " nie  istnieje");
    }

    @Test
    void shouldReturnUserById() {
        //given:
        UserClass userClass = new UserClass("name", "lastname", "email", "password",
                "rPassword");
        Mockito.when(userRepository.findOneById(userClass.getId()))
                .thenReturn(Optional.of(userClass));

        //when:
        UserClass actual = testRoleService.findUserById(userClass.getId());

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getFirstName()).isEqualTo(userClass.getFirstName());
    }

    @Captor
    private ArgumentCaptor<UserClass> userArgumentCaptor;

    @Test
    void testAssignRoleToUser() {
        //given:
        UserClass userClass = Instancio.create(UserClass.class);
        Role role = new Role("role1");
        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.of(role));
        Mockito.when(userRepository.findOneById(userClass.getId()))
                .thenReturn(Optional.of(userClass));

        role.assignUser(userClass);
        userClass.addRole(role);

        //when:
        testRoleService.assignRoleToUser(role.getId(), userClass.getId());

        //then:
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
    }

    @Test
    void shouldThrowRoleByNameNotFoundExceptionWhenChangeRoleToUser() {
        //given:
        Role role = new Role("role1");
        UserClass user = new UserClass("name", "lastname", "email", "password",
                "rPassword");
        UpdateRoleDto newRoleDto = new UpdateRoleDto("new");

        Mockito.when(roleRepository.findByName(newRoleDto.getName()))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findOneById(user.getId()))
                .thenReturn(Optional.of(user));

        // when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.changeRoleToUser(role.getId(), user.getId(), newRoleDto));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Rola z taką nazwą: " + newRoleDto.getName() + " nie istnieje");
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenChangeRoleToUser() {
        //given:
        Role role = new Role("role1".toUpperCase());
        UserClass user = new UserClass("name", "lastname", "email", "password",
                "rPassword");
        UpdateRoleDto newRoleDto = new UpdateRoleDto("role1".toUpperCase());

        Mockito.when(roleRepository.findByName(newRoleDto.getName()))
                .thenReturn(Optional.of(role));

        Mockito.when(userRepository.findOneById(user.getId()))
                .thenReturn(Optional.empty());

        // when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.changeRoleToUser(role.getId(), user.getId(), newRoleDto));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("User z tym id: " + user.getId() + " nie  istnieje");
    }

    @Test
    void testChangeRoleToUser() {
        //given:
        Role role = new Role("role1".toUpperCase());
        UserClass user = new UserClass("name", "lastname", "email", "password",
                "rPassword");
        UpdateRoleDto newRoleDto = new UpdateRoleDto("role1".toUpperCase());

        Mockito.when(userRepository.findOneById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(roleRepository.findByName(newRoleDto.getName()))
                .thenReturn(Optional.of(role));

        user.clearAssignRole();
        user.addRole(role);
        role.assignUser(user);
        // when:
        testRoleService.changeRoleToUser(role.getId(), user.getId(), newRoleDto);

        //then:
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
    }

    @Test
    void shouldThrowRoleByIdNotFoundExceptionIfDeleteRole() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(roleRepository.findOneById(id))
                .thenReturn(Optional.empty());

        // when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.deleteRoleById(id));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Rola z takim id:  " + id + " nie istnieje");
    }

    @Captor
    private ArgumentCaptor<Role> roleArgumentCaptor;

    @Test
    void testDeleteRoleById() {
        //given:
        Role role = new Role("role1");
        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.of(role));

        //when:
        testRoleService.deleteRoleById(role.getId());

        //then:
        Mockito.verify(roleRepository).delete(roleArgumentCaptor.capture());
    }

    @Test
    void shouldThrowRoleNotFoundExceptionWhenUpdateRole() {
        //given:
        UUID id = UUID.randomUUID();
        NewRoleDto newRoleDto = Instancio.create(NewRoleDto.class);
        Mockito.when(roleRepository.findOneById(id))
                .thenReturn(Optional.empty());

        // when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testRoleService.updateRoleById(id, newRoleDto));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Rola z takim id:  " + id + " nie istnieje");
    }

    @Test
    void testUpdateRoleById() {
        //given:
        Role role = new Role("role1".toUpperCase());
        NewRoleDto newRoleDto = new NewRoleDto("role1".toUpperCase());

        Mockito.when(roleRepository.findOneById(role.getId()))
                .thenReturn(Optional.of(role));

        Mockito.when(roleMapper.mapNewRoleDtoToEntity(newRoleDto))
                .thenReturn(role);

        //when:
        testRoleService.updateRoleById(role.getId(), newRoleDto);

        //then:
        Mockito.verify(roleRepository).save(roleArgumentCaptor.capture());
    }

}
