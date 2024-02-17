package pl.elgrandeproject.elgrande.entities.role;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository testRoleRepository;

    @Test
    void shouldReadRolesFromDb() {
        //given:
        UserClass user = new UserClass("Anna", "Nowak", "an@test.com",
                "$2a$10$3M.RqgFxw2P1d0/00bl74elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3q",
                "$8a$10$3M.ZqgFxw2P1d0/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a");

        //when:
        List<Role> actual = testRoleRepository.findAllBy();

        //then:
        Assertions.assertThat(actual.size()).isEqualTo(3);
        Assertions.assertThat(actual.get(0).getId())
                .isEqualTo(UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203"));
        Assertions.assertThat(actual.get(0).getName()).isEqualTo("ADMIN");
        Assertions.assertThat(actual.get(1).getName()).isEqualTo("USER");
        Assertions.assertThat(actual.get(2).getName()).isEqualTo("USER7");
        Assertions.assertThat(actual.get(0).getUsers().size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getUsers()).contains(user);
    }

    @Test
    void shouldReadRoleByIdFromDb() {
        //given:
        UserClass user = new UserClass("Anna", "Nowak", "an@test.com",
                "$2a$10$3M.RqgFxw2P1d0/00bl74elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3q",
                "$8a$10$3M.ZqgFxw2P1d0/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a");

        UUID id = UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203");

        //when:
        Optional<Role> actual = testRoleRepository.findOneById(id);

        //then:
        Assertions.assertThat(actual.get().getId()).isEqualTo(UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203"));
        Assertions.assertThat(actual.get().getName()).isEqualTo("ADMIN");
        Assertions.assertThat(actual.get().getUsers().size()).isEqualTo(1);
        Assertions.assertThat(actual.get().getUsers().contains(user));
    }

    @Test
    void shouldReadRoleByNameFromDb() {
        //given:
        String name = "USER";

        //when:
        Optional<Role> actual = testRoleRepository.findByName(name);

        //then:
        Assertions.assertThat(actual.get().getId()).isEqualTo(UUID.fromString("b8081184-c21c-40ce-b806-32cdf73a82db"));
        Assertions.assertThat(actual.get().getName()).isEqualTo("USER");
        Assertions.assertThat(actual.get().getUsers().size()).isEqualTo(1);
    }
}