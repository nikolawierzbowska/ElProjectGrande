package pl.elgrandeproject.elgrande.entities.opinion;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.elgrandeproject.elgrande.config.SecurityConfiguration;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.registration.Principal;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.elgrandeproject.elgrande.config.SecurityConfiguration.ADMIN;
import static pl.elgrandeproject.elgrande.config.SecurityConfiguration.USER;

@WebMvcTest(controllers = OpinionController.class)
@Import(SecurityConfiguration.class)
class OpinionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpinionService opinionService;

    @MockBean
    private OpinionRepository opinionRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void shouldReadAllOpinionsDto() throws Exception {
        //given:
        Course course = new Course("tes-name");
        Opinion opinion1 = new Opinion("test- desc1", "Anna");
        Opinion opinion2 = new Opinion("test- desc2", "Mateusz");

        OpinionDto opinionDto1 = new OpinionDto(opinion1.getId(), opinion1.getDescription(), opinion1.getUserName());
        OpinionDto opinionDto2 = new OpinionDto(opinion2.getId(), opinion2.getDescription(), opinion2.getUserName());

        List<OpinionDto> opinionListDto = List.of(opinionDto1, opinionDto2);

        Mockito.when(opinionService.getAllOpinionsByCourseId(course.getId()))
                .thenReturn(opinionListDto);

        //when:
        ResultActions response = mockMvc.perform(get("/api/v1/courses/" + course.getId() + "/opinions"));

        //then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(opinionListDto.get(0).id().toString()))
                .andExpect(jsonPath("$[0].description").value(opinionListDto.get(0).description()))
                .andExpect(jsonPath("$[0].userName").value(opinionListDto.get(0).userName()))
                .andExpect(jsonPath("$[1].id").value(opinionListDto.get(1).id().toString()))
                .andExpect(jsonPath("$[1].description").value(opinionListDto.get(1).description()))
                .andExpect(jsonPath("$[1].userName").value(opinionListDto.get(1).userName()));
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReadOpinionById() throws Exception {
        //given:
        Course course = new Course("tes-name");
        Opinion opinion1 = new Opinion("test- desc1", "Anna");

        OpinionDto opinionDto1 = new OpinionDto(opinion1.getId(), opinion1.getDescription(), opinion1.getUserName());

        Mockito.when(opinionService.getOpinionDtoById(course.getId(), opinion1.getId()))
                .thenReturn(opinionDto1);

        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/courses/" + course.getId() + "/opinions/" + opinion1.getId()));

        //then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(opinionDto1.id().toString()))
                .andExpect(jsonPath("$.description").value(opinionDto1.description()))
                .andExpect(jsonPath("$.userName").value(opinionDto1.userName()));
    }

    @Test
    void shouldReturnUnauthorizedOpinionById() throws Exception {
        //given:
        Course course = new Course("tes-name");
        Opinion opinion1 = new Opinion("test- desc1", "Anna");

        OpinionDto opinionDto1 = new OpinionDto(opinion1.getId(), opinion1.getDescription(), opinion1.getUserName());

        Mockito.when(opinionService.getOpinionDtoById(course.getId(), opinion1.getId()))
                .thenReturn(opinionDto1);

        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/courses/" + course.getId() + "/opinions/" + opinion1.getId()));

        //then:
        response.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldReturn404NotFoundOpinionById() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();
        UUID opinionId = UUID.randomUUID();

        Mockito.when(opinionService.getOpinionDtoById(courseId, opinionId))
                .thenThrow(new OpinionNotFoundException("Nie znaleziono opini o takim id: " + opinionId));

        //when:
        ResultActions response = mockMvc.perform(
                get("/api/v1/courses/" + courseId + "/opinions/" + opinionId));

        //then:
        response.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = USER)
    void shouldCreateOpinion() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();
        NewOpinionDto newOpinionDto = new NewOpinionDto("new-opinion", "Ala");
        OpinionDto opinionDto = new OpinionDto(
                UUID.randomUUID(), newOpinionDto.description(), newOpinionDto.userName());
        Principal principal = new Principal();

        Mockito.when(opinionService.saveNewOpinion(courseId, newOpinionDto, principal))
                .thenReturn(opinionDto);

        //when:
        ResultActions response = mockMvc.perform(
                post("/api/v1/courses/" + courseId + "/opinions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "new-opinion",
                                    "userName": "Ala"
                                }
                                """)
        );
        //then:
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = USER)
    void shouldReturn409WhenInsertEmptyOpinion() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();
        //when:
        ResultActions response = mockMvc.perform(
                post("/api/v1/courses/" + courseId + "/opinions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "",
                                    "userName": "Ala"
                                }
                                """)
        );
        //then:
        response.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = ADMIN)
    void shouldDeleteOpinion() throws Exception {
        //given:
        UUID courseId = UUID.randomUUID();
        UUID opinionId = UUID.randomUUID();
        //when:
        ResultActions response = mockMvc.perform(
                delete("/api/v1/admin/courses/" + courseId + "/opinions/" + opinionId));

        //then:
        response.andExpect(status().isOk());
    }
}