package pl.elgrandeproject.elgrande.entities.role;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

class RoleTest {
    @Test
    void testAssignUser() {

        //given:
        Role role = new Role("test-roleName");
        UserClass userClass = new UserClass("test-firstName","test-lastName", "test-email",
                "test-password", "test-rPassword");
        //when:
        role.assignUser(userClass);

        //then:
        Assertions.assertThat(role.getUsers().size()).isEqualTo(1);
        Assertions.assertThat(role.getUsers().contains(userClass)).isTrue();


    }
}