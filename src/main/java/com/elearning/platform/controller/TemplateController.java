package com.elearning.platform.controller;

import com.elearning.platform.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @GetMapping("/discover")
    public String discover() {
        return "discover";
    }

    @GetMapping("/courses")
    public String courses() {
        return "courses/courses";
    }

    @GetMapping("/tutors")
    public String tutors() {
        return "tutors/tutors";
    }

    @GetMapping("/teachers")
    public String teachers() {
        return "tutors/tutors";
    }
}