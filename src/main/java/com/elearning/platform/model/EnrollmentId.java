package com.elearning.platform.model;

import java.io.Serializable;
import java.util.Objects;

public class EnrollmentId implements Serializable {
    private Long student;
    private Long course;

    public EnrollmentId() {}

    public EnrollmentId(Long student, Long course) {
        this.student = student;
        this.course = course;
    }

    public Long getStudent() {
        return student;
    }

    public void setStudent(Long student) {
        this.student = student;
    }

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentId that = (EnrollmentId) o;
        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
