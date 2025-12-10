package com.elearning.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    
    private Long studentId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private Integer progress;
    private Boolean completed;
}