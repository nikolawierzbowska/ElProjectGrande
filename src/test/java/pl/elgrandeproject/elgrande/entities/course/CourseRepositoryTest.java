package pl.elgrandeproject.elgrande.entities.course;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository testCourseRepository;

    @Test
    void shouldReadAllCoursesFromDb() {
        //when:
        List<Course> actual = testCourseRepository.findAll();

        //then:
        Course course1 = new Course("KURS Z MATEMATYKI");
        Course course2 = new Course("KURS");

        Assertions.assertThat(actual.get(0).getName()).isEqualTo(course1.getName());
        Assertions.assertThat(actual.get(1).getName()).isEqualTo(course2.getName());
        Assertions.assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void shouldReadOneCourseByIdFromDb() {
        //given:
        UUID id = UUID.fromString("f8081184-c21c-40ce-b806-32cdf73a82da");

        //when:
        Optional<Course> actual = testCourseRepository.findOneById(id);

        //then:
        Assertions.assertThat(actual.get().getId()).isEqualTo(id);
        Assertions.assertThat(actual.get().getName()).isEqualTo("KURS");
    }

    @Test
    void shouldReadOneCourseByNameFromDb() {
        //given:
        String name = "KURS";

        //when:
        Optional<Course> actual = testCourseRepository.findOneByName(name);

        //then:
        Assertions.assertThat(actual.get().getId()).isEqualTo(UUID.fromString("f8081184-c21c-40ce-b806-32cdf73a82da"));
        Assertions.assertThat(actual.get().getName()).isEqualTo("KURS");
    }
}