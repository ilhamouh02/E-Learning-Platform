package com.elearning.platform.dto;

import javax.validation.constraints.NotBlank;

public class LessonDto {
    
    private Long id;

    @NotBlank(message = "Le titre de la leçon est obligatoire")
    private String title;

    private String content;
    private String videoUrl;
    private Integer orderIndex;
    private Long courseId;
    
    // Constructeurs
    public LessonDto() {}
    
    public LessonDto(Long id, String title, String content, String videoUrl, Integer orderIndex, Long courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.orderIndex = orderIndex;
        this.courseId = courseId;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}