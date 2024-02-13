package pl.elgrandeproject.elgrande.entities.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.role.Role;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();
    //given:
    //when:
    //then:


    @Test
    void shouldMapEntityToDto() {
        //given:
        UserClass userClass =new UserClass("test-firstName", "test-lastName", "test-email",
                "test-password", "test-rPassword");
        Role role1 = new Role("test-nameRole1");
        Role role2 = new Role("test-nameRole2");

        userClass.addRole(role1);
        userClass.addRole(role2);

        //when:
        UserDto actual = userMapper.mapEntityToDto(userClass);

        //then:
        Assertions.assertThat(actual.id()).isEqualTo(userClass.getId());
        Assertions.assertThat(actual.firstName()).isEqualTo("test-firstName");
        Assertions.assertThat(actual.lastName()).isEqualTo("test-lastName");
        Assertions.assertThat(actual.email()).isEqualTo("test-email");
        Assertions.assertThat(actual.password()).isEqualTo("test-password");
        Assertions.assertThat(actual.repeatedPassword()).isEqualTo("test-rPassword");
        Assertions.assertThat(actual.userRoles()).isNotNull();
        Assertions.assertThat(actual.userRoles()).containsExactlyInAnyOrder(
                new UserDto.userRole(role1.getId(),"test-nameRole1"),
                new UserDto.userRole(role2.getId(),"test-nameRole2")
        );


    }


}