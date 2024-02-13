package pl.elgrandeproject.elgrande.entities.course;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.opinion.Opinion;

class CourseTest {

    @Test
    void shouldReturnCorrectNumberOfOpinions() {
        //given:
        Course course = new Course("test-nameCourse");
        Opinion opinion = new Opinion("test-description","test-userName");
        course.addOpinion(opinion);

        //when:
        Opinion opinion2 = new Opinion("test-description2","test-userName2");
        course.addOpinion(opinion2);

        //than:
        Assertions.assertThat(course.getOpinions().size()).isEqualTo(2);
        Assertions.assertThat(course.getOpinions()).contains(opinion2);



    }
}