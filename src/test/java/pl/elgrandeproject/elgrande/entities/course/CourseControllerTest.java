package pl.elgrandeproject.elgrande.entities.course;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.elgrandeproject.elgrande.config.SecurityConfiguration;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.opinion.Opinion;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.elgrandeproject.elgrande.config.SecurityConfiguration.ADMIN;

@WebMvcTest(controllers = CourseController.class)
@Import(SecurityConfiguration.class)
@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadAllCoursesDto() throws Exception {
        //given:
        CourseDto courseDto1 = new CourseDto(UUID.randomUUID(), "test-name", null);
        Opinion opinion = new Opinion("test-description", "Ala");
        CourseDto.OpinionsToCourse opinionToCourse = new CourseDto.OpinionsToCourse(opinion.getId(), opinion.getDescription());

        List<CourseDto.OpinionsToCourse> opinionsList = List.of(opinionToCourse);

        CourseDto courseDto2 = new CourseDto(UUID.randomUUID(), "test-name", opinionsList);
        List<CourseDto> listCoursesDto = List.of(courseDto1, courseDto2);

        Mockito.when(courseService.getAllCourses())
                .thenReturn(listCoursesDto);

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/courses"));

        //then:
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(listCoursesDto.get(0).id().toString()))
                .andExpect(jsonPath("$[0].name").value(listCoursesDto.get(0).name()))
                .andExpect(jsonPath("$[1].id").value(listCoursesDto.get(1).id().toString()))
                .andExpect(jsonPath("$[1].name").value(listCoursesDto.get(1).name()))
                .andExpect(jsonPath("$[1].opinions.size()").value(1))
                .andExpect(jsonPath("$[1].opinions[0].id")
                        .value(listCoursesDto.get(1).opinions().get(0).id().toString()))
                .andExpect(jsonPath("$[1].opinions[0].description")
                        .value(listCoursesDto.get(1).opinions().get(0).description()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404WhenCourseDtoByIdNotExist() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();
        Mockito.when(courseService.getCourseDtoById(courseId))
                .thenThrow(new CourseNotFoundException("Kurs z takim ID " + courseId + " nie został znaleziony"));

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/courses/id/" + courseId));

        //then:
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("Kurs z takim ID " + courseId +
                        " nie został znaleziony"));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadCourseDtoById() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();
        CourseDto courseDto1 = new CourseDto(UUID.randomUUID(), "test-name", null);

        Mockito.when(courseService.getCourseDtoById(courseId))
                .thenReturn(courseDto1);

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/courses/id/" + courseId));

        //then:
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDto1.id().toString()))
                .andExpect(jsonPath("$.name").value(courseDto1.name()))
                .andExpect(jsonPath("$.opinions").value(courseDto1.opinions()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404WhenCourseDtoByNameNotExist() throws Exception {
        //given:
        String name = "name-course";
        Mockito.when(courseService.getCourseDtoByName(name))
                .thenThrow(new CourseNotFoundException("Kurs z taką nazwą: " + name + " nie został znaleziony"));

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/courses/" + name));

        //then:
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("Kurs z taką nazwą: " + name +
                        " nie został znaleziony"));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadCourseDtoByName() throws Exception {
        //given:
        String name = "test-name";
        CourseDto courseDto1 = new CourseDto(UUID.randomUUID(), "test-name", null);

        Mockito.when(courseService.getCourseDtoByName(name))
                .thenReturn(courseDto1);

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/admin/courses/" + name));

        //then:
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDto1.id().toString()))
                .andExpect(jsonPath("$.name").value(courseDto1.name()))
                .andExpect(jsonPath("$.opinions").value(courseDto1.opinions()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturnCourseDto() throws Exception {
        //given:
        NewCourseDto newCourseDto = new NewCourseDto("name-course");
        CourseDto courseDto1 = new CourseDto(UUID.randomUUID(), "name-course", null);
        Mockito.when(courseService.saveNewCourse(newCourseDto))
                .thenReturn(courseDto1);

        //when:
        ResultActions response = mockMvc.perform(post("/api/v1/admin/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "name-course"
                        }
                        """));

        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404CourseNameIsEmpty() throws Exception {
        //when:
        ResultActions response = mockMvc.perform(post("/api/v1/admin/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                  "name": ""
                                }
                                """));
        //then:
        response.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldDeleteCourse() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();

        //when:
        ResultActions response = mockMvc.perform(delete("/api/v1/admin/courses/" + courseId));

        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn400WhenInsertEmptyName() throws Exception {
        //given:
        Course course = new Course("name");
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn( Optional.of(course));

        //when:
        ResultActions response = mockMvc.perform(patch("/api/v1/admin/courses/"+ course.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": ""
                        }
                        """));
        //then:
        response.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn409WhenInsertEmptyName() throws Exception {
        //given:
        Course course = new Course("name");
        Mockito.when(courseRepository.findOneByName(course.getName()))
                .thenReturn( Optional.of(course));
        //when:
        ResultActions response = mockMvc.perform(patch("/api/v1/admin/courses/"+ course.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": ""
                        }
                        """));
        //then:
        response.andExpect(status().isBadRequest());
    }
}