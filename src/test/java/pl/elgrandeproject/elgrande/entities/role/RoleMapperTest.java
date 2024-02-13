package pl.elgrandeproject.elgrande.entities.role;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

class RoleMapperTest {

    private final RoleMapper testRoleMapper = new RoleMapper();

    @Test
    void shouldMapNewRoleDtoToEntity() {
        //given:
        NewRoleDto newRoleDto = new NewRoleDto("test-name");

        //when:
        Role actual = testRoleMapper.mapNewRoleDtoToEntity(newRoleDto);

        //then:
        Assertions.assertThat(actual.getName()).isEqualTo("test-name");
        Assertions.assertThat(actual.getId()).isNotNull();
        
    }

    @Test
    void shouldMapEntityToDto() {
        //given:
        Role role = new Role("test-nameRole");
        UserClass user1 = new UserClass("firstName1", "lastName1", "email-1","password1", "rPassword1");
        UserClass user2 = new UserClass("firstName2", "lastName2", "email-2","password2", "rPassword2");
        role.assignUser(user1);
        role.assignUser(user2);

        //when:
        RoleDto actual = testRoleMapper.mapEntityToDto(role);

        //then:
        Assertions.assertThat(actual.id()).isEqualTo(role.getId());
        Assertions.assertThat(actual.name()).isEqualTo("test-nameRole");
        Assertions.assertThat(actual.users()).containsExactlyInAnyOrder(
                new RoleDto.UserNameId(user1.getId(), "email-1"),
                new RoleDto.UserNameId(user2.getId(),"email-2")

        );





    }
}