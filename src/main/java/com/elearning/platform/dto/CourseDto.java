package com.elearning.platform.dto;

import javax.validation.constraints.NotBlank;

/**
 * CourseDto - Data Transfer Object pour Course
 * Chemin: src/main/java/com/elearning/platform/dto/CourseDto.java
 * 
 * Utilisé pour créer/modifier les cours
 */
public class CourseDto {
    
    private Long id;

    @NotBlank(message = "Le titre du cours est obligatoire")
    private String title;

    private String description;
    private String category;
    private Long teacherId;

    // Constructeurs
    public CourseDto() {}

    public CourseDto(Long id, String title, String description, String category, Long teacherId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.teacherId = teacherId;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}