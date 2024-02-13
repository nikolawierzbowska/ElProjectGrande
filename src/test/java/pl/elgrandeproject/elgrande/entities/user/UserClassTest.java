package pl.elgrandeproject.elgrande.entities.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.role.Role;

import static org.junit.jupiter.api.Assertions.*;

class UserClassTest {

    private final UserClass userClass = new UserClass("test-firstName","test-lastName", "test-email",
            "test-password", "test-rPassword");


    @Test
    void testAddRole() {
        //given:
        Role role = new Role("test-roleName");
        Role role1 = new Role("test-roleName1");
        //when:
        userClass.addRole(role);
        userClass.addRole(role1);
        //then:
        Assertions.assertThat(userClass.getRoles().contains(role)).isTrue();
        Assertions.assertThat(userClass.getRoles().size()).isEqualTo(2);
    }

    @Test
    void testClearAssignRole() {
        //given:
        Role role = new Role("test-roleName");
        Role role1 = new Role("test-roleName1");
        userClass.addRole(role);
        userClass.addRole(role1);

        //when:
        userClass.clearAssignRole();

        //then:
        Assertions.assertThat(userClass.getRoles().size()).isEqualTo(0);
        Assertions.assertThat(userClass.getRoles()).isEmpty();
    }
}