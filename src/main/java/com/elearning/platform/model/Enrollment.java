package com.elearning.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @Column(name = "enrollmentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @Column(name = "date")
    private LocalDate enrollmentDate;

    @Column(name = "progress")
    private Integer progress = 0;

    @Column(name = "completed")
    private Boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    public Enrollment(LocalDate enrollmentDate, User student, Course course) {
        this.enrollmentDate = enrollmentDate;
        this.student = student;
        this.course = course;
        this.progress = 0;
        this.completed = false;
    }
}
