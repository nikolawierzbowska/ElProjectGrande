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

    @GetMapping("/id/{courseId}")
    public CourseDto getCourseById(@PathVariable UUID courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping("/{courseName}")
    public CourseDto getCourseById(@PathVariable String courseName) {
        return courseService.getCourseByName(courseName);
    }

    @PostMapping
    public CourseDto createNewCourse(@Valid @RequestBody NewCourseDto newCourseDto) {
        return courseService.saveNewCourse(newCourseDto);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourseById(@PathVariable UUID courseId) {
        courseService.deleteCourseById(courseId);

    }

    @PatchMapping("/{courseId}")
    public void updateCourseById(@Valid @PathVariable UUID courseId, @RequestBody  NewCourseDto updateCourseDto){
        courseService.updateCourseId(courseId, updateCourseDto);
    }

}
