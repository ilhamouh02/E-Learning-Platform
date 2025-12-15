package com.elearning.platform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité Enrollment - Inscription d'un étudiant à un cours
 * Chemin: src/main/java/com/elearning/platform/model/Enrollment.java
 * 
 * ✅ CORRIGÉ FINAL: EnrollmentId est une classe SÉPARÉE et publique
 * Utilise @EmbeddedId pour la clé composite
 */
@Entity
@Table(name = "enrollments", indexes = {
    @Index(name = "idx_student_id", columnList = "student_id"),
    @Index(name = "idx_course_id", columnList = "course_id")
})
public class Enrollment {
    
    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column
    private Integer progress = 0;

    @Column
    private Boolean completed = false;

    // Constructeurs
    public Enrollment() {
        this.enrolledAt = LocalDateTime.now();
        this.progress = 0;
        this.completed = false;
    }

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.id = new EnrollmentId(student.getId(), course.getId());
        this.enrolledAt = LocalDateTime.now();
        this.progress = 0;
        this.completed = false;
    }

    @PrePersist
    protected void onCreate() {
        if (enrolledAt == null) {
            enrolledAt = LocalDateTime.now();
        }
        if (progress == null) {
            progress = 0;
        }
        if (completed == null) {
            completed = false;
        }
        if (id == null) {
            id = new EnrollmentId();
        }
        if (student != null && course != null) {
            id.setStudentId(student.getId());
            id.setCourseId(course.getId());
        }
    }

    // Getters et Setters
    public EnrollmentId getId() { return id; }
    public void setId(EnrollmentId id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { 
        this.student = student;
        if (id == null) id = new EnrollmentId();
        if (student != null) id.setStudentId(student.getId());
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { 
        this.course = course;
        if (id == null) id = new EnrollmentId();
        if (course != null) id.setCourseId(course.getId());
    }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", student=" + (student != null ? student.getId() : null) +
                ", course=" + (course != null ? course.getId() : null) +
                ", progress=" + progress +
                '}';
    }
}