package com.elearning.platform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "enrollments")
@IdClass(EnrollmentId.class)
public class Enrollment {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column
    private Integer progress = 0;

    @Column
    private Boolean completed = false;

    // Constructeurs
    public Enrollment() {}

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
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
    }

    // Getters et Setters
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}

// Classe pour la clé composite
class EnrollmentId implements Serializable {
    private Long student;
    private Long course;

    public EnrollmentId() {}

    public EnrollmentId(Long student, Long course) {
        this.student = student;
        this.course = course;
    }

    public Long getStudent() { return student; }
    public void setStudent(Long student) { this.student = student; }

    public Long getCourse() { return course; }
    public void setCourse(Long course) { this.course = course; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentId that = (EnrollmentId) o;
        return Objects.equals(student, that.student) &&
               Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}