package com.elearning.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_attempt", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"quizId", "userId"})
})
public class QuizAttempt {
    @Id
    @Column(name = "attemptId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "completedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quizId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @PrePersist
    protected void onCreate() {
        completedAt = LocalDateTime.now();
    }
}
