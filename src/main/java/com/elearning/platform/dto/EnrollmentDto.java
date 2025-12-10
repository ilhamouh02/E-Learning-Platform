package com.elearning.platform.dto;

import java.time.LocalDateTime;

public class EnrollmentDto {
    
    private Long studentId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private Integer progress;
    private Boolean completed;

    // Constructeurs
    public EnrollmentDto() {}

    public EnrollmentDto(Long studentId, Long courseId, LocalDateTime enrolledAt, Integer progress, Boolean completed) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
        this.progress = progress;
        this.completed = completed;
    }

    // Getters et Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}