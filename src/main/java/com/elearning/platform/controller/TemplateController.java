package com.elearning.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/courses")
    public String getCourses() {
        return "courses/courses";
    }
}
