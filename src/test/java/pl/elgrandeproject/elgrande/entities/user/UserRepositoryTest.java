package pl.elgrandeproject.elgrande.entities.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository testUserRepository;

    @Test
    void shouldReadAllUsersFromDb() {
        //when:
        List<UserClass> actual = testUserRepository.findAllBy();

        //then:
        UserClass user1 = new UserClass("Anna", "Nowak", "an@test.com",
                "$2a$10$3M.RqgFxw2P1d0/00bl74elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3q",
                "$8a$10$3M.ZqgFxw2P1d0/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a");

        UserClass user2 = new UserClass("Jan", "Nowak", "jn@test.com",
                "$8f$10$3M.ZqgFxf2P1d0/00bl54flBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a",
                "$1a$10$3M.ZqeFxw2P1d2/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3d");

        Assertions.assertThat(actual.get(0).getFirstName()).isEqualTo(user1.getFirstName());
        Assertions.assertThat(actual.get(1).getFirstName()).isEqualTo(user2.getFirstName());
        Assertions.assertThat(actual.get(0).getLastName()).isEqualTo(user1.getLastName());
        Assertions.assertThat(actual.get(1).getLastName()).isEqualTo(user2.getLastName());
        Assertions.assertThat(actual.get(0).getEmail()).isEqualTo(user1.getEmail());
        Assertions.assertThat(actual.get(1).getEmail()).isEqualTo(user2.getEmail());
        Assertions.assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void shouldReadOneUserByIdFromDb() {
        //given:
        UserClass user = new UserClass("Anna", "Nowak", "an@test.com",
                "$2a$10$3M.RqgFxw2P1d0/00bl74elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3q",
                "$8a$10$3M.ZqgFxw2P1d0/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a");

        //when:
        Optional<UserClass> actual = testUserRepository
                .findOneById(UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294"));

        //then:
        Assertions.assertThat(actual.get().getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(actual.get().getLastName()).isEqualTo(user.getLastName());
        Assertions.assertThat(actual.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldNotReadUserWhenIsSoftDeleted() {
        //given:
        UUID id = UUID.fromString("04a4c4b8-c361-4681-bd4b-13c332f0ba38");

        //when:
        Optional<UserClass> actual = testUserRepository.findOneById(id);

        //then:
        Assertions.assertThat(actual).isEmpty();
    }
}