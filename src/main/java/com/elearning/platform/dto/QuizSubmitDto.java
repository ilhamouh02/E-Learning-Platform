package com.elearning.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitDto {
    @NotNull(message = "L'ID du quiz est obligatoire")
    private Long quizId;

    @NotNull(message = "Les réponses sont obligatoires")
    @JsonProperty("answers")
    private Map<Long, String> answers; // questionId -> answer

    @Min(value = 0)
    @Max(value = 100)
    private Integer score;
}
