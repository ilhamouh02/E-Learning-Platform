package com.elearning.platform.controller;

import com.elearning.platform.model.User;
import com.elearning.platform.repositories.UserRepository;
import com.elearning.platform.dto.CourseDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.repositories.CourseRepository;
import com.elearning.platform.repositories.EnrollmentRepository;
import com.elearning.platform.services.core.impl.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public CourseController(CourseService courseService, CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    // === AJOUTER UN COURS (admin ou enseignant) ===
    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String addCourseForm(Model model, Authentication auth) {
        model.addAttribute("course", new CourseDto());
        model.addAttribute("currentUser", userRepository.findByUsername(auth.getName()));
        return "courses/course-add";
    }

   @PostMapping("/add")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
public String saveCourse(CourseDto courseDto, Authentication auth) {
    User currentUser = userRepository.findByUsername(auth.getName());
    courseDto.setTeacher(currentUser);
    courseService.create(courseDto);
    return "redirect:/courses";
}

    // === MODIFIER UN COURS ===
    @GetMapping("/edit/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @courseService.isOwner(#courseId, authentication.name)")
    public String editCourseForm(@PathVariable Long courseId, Model model) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        model.addAttribute("course", course);
        return "courses/course-edit";
    }

    @PostMapping("/edit/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @courseService.isOwner(#courseId, authentication.name)")
    public String updateCourse(@PathVariable Long courseId, Course course, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName());
        course.setTeacher(currentUser);
        courseService.update(course, courseId);
        return "redirect:/courses/{courseId}";
    }

    // === LISTE DES COURS ===
    @GetMapping
    public String getCoursesList(Model model) {
        List<Course> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        return "courses/courses";
    }

    // === SUPPRIMER UN COURS ===
    @GetMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        courseService.delete(course);
        return "redirect:/courses";
    }

    // === DÉTAIL D'UN COURS ===
    @GetMapping("/{courseId}")
    public String getCourseDetail(@PathVariable Long courseId, Authentication authentication, Model model) {
        String username = authentication.getName();
        Course course = courseRepository.findById(courseId).orElseThrow();
        User user = userRepository.findByUsername(username);

        boolean enrolled = enrollmentRepository
                .findByStudent_UserIdAndCourse_CourseId(user.getUserId(), courseId)
                .isPresent();

        model.addAttribute("course", course);
        model.addAttribute("enrolled", enrolled);
        return "courses/course-detail";
    }
}