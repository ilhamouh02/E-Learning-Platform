package com.elearning.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question {
    @Id
    @Column(name = "questionId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(name = "questionText", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(name = "options", columnDefinition = "JSON")
    private String options; // JSON array format

    @Column(name = "correctAnswer", nullable = false)
    private String correctAnswer;

    @Column(name = "points")
    private Integer points = 1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quizId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Quiz quiz;
}
