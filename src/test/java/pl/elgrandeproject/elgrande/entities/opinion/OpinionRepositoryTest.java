package pl.elgrandeproject.elgrande.entities.opinion;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class OpinionRepositoryTest {

    @Autowired
    private OpinionRepository testOpinionRepository;

    @Test
    void shouldReadOpinionsByCourseIdFromDb() {
        //given:
        UUID courseId = UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294");

        //when:
        List<Opinion> actual = testOpinionRepository.findOpinion(courseId);

        //then:
        Assertions.assertThat(actual.size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getId())
                .isEqualTo(UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203"));
        Assertions.assertThat(actual.get(0).getUserName()).isEqualTo("Anna");
        Assertions.assertThat(actual.get(0).getCourses().getName()).isEqualTo("KURS Z MATEMATYKI");
        Assertions.assertThat(actual.get(0).getDescription()).isEqualTo("opinia kursu");
    }

    @Test
    void shouldReadOpinionFromDb() {
        //given:
        UUID courseId = UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294");
        UUID opinionId = UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203");

        //when:
        Optional<Opinion> actual = testOpinionRepository.findOneById(courseId, opinionId);

        //then:
        Assertions.assertThat(actual.get().getDescription()).isEqualTo("opinia kursu");
        Assertions.assertThat(actual.get().getUserName()).isEqualTo("Anna");
        Assertions.assertThat(actual.get().getCourses().getName()).isEqualTo("KURS Z MATEMATYKI");
    }
}