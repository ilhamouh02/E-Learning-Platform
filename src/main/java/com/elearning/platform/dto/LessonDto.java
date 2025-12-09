package com.elearning.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long lessonId;

    @NotBlank(message = "Le titre de la leçon est obligatoire")
    private String title;

    private String content;
    private String videoUrl;

    @Min(value = 0, message = "L'index de la leçon doit être >= 0")
    private Integer orderIndex;

    private Long courseId;
}
