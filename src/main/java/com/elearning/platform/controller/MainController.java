package com.elearning.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/discover")
    public String discover() {
        return "discover";
    }

    @GetMapping("/courses/{id}")
    public String courseDetail() {
        return "courses/course-detail";
    }

    @GetMapping("/quiz")
    public String quiz() {
        return "quiz";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard-student";
    }
}
