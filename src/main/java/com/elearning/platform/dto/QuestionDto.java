package com.elearning.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long questionId;

    @NotBlank(message = "Le texte de la question est obligatoire")
    private String questionText;

    private String options; // JSON array format

    @NotBlank(message = "La réponse correcte est obligatoire")
    private String correctAnswer;

    @Min(value = 1, message = "Les points doivent être >= 1")
    private Integer points;

    private Long quizId;
}
