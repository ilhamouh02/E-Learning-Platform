package com.elearning.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private Long quizId;

    @NotBlank(message = "Le titre du quiz est obligatoire")
    private String title;

    @Min(value = 1, message = "La durée du quiz doit être >= 1 minute")
    private Integer timeLimit;

    @Min(value = 0, message = "Le score de passage doit être >= 0")
    private Integer passingScore;

    private Long lessonId;
}
