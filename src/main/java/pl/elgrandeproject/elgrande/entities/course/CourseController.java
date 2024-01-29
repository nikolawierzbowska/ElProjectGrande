package pl.elgrandeproject.elgrande.entities.course;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    public CourseDto getCourseById(@PathVariable UUID courseId) {
        return courseService.getCourseById(courseId);
    }

    @PostMapping
    public CourseDto createNewCourse(@Valid @RequestBody NewCourseDto newCourseDto) {
        return courseService.saveNewCourse(newCourseDto);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourseById(@PathVariable UUID courseId) {
        courseService.deleteCourseById(courseId);

    }
}
