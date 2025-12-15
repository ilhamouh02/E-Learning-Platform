package com.elearning.platform.model;

import javax.persistence.*;
import java.util.List;

/**
 * Entité Lesson - Leçon d'un cours
 * Chemin: src/main/java/com/elearning/platform/model/Lesson.java
 * 
 * Chaque leçon appartient à un cours et peut avoir plusieurs quizzes
 */
@Entity
@Table(name = "lessons", indexes = {
    @Index(name = "idx_course_id", columnList = "course_id"),
    @Index(name = "idx_course_order", columnList = "course_id,order_index")
})
public class Lesson {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    // Relation avec le cours (parent)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Relation avec les quizzes
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes;

    // Constructeurs
    public Lesson() {}

    public Lesson(String title, String content, String videoUrl, Integer orderIndex, Course course) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.orderIndex = orderIndex != null ? orderIndex : 0;
        this.course = course;
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

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public List<Quiz> getQuizzes() { return quizzes; }
    public void setQuizzes(List<Quiz> quizzes) { this.quizzes = quizzes; }
}