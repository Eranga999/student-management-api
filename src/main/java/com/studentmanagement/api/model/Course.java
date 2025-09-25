package com.studentmanagement.api.model;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import com.google.cloud.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    private String id;
    
    @NotBlank(message = "Course name is required")
    private String name;
    
    @NotNull(message = "Fee is required")
    @DecimalMin(value = "0.0", message = "Fee must be positive")
    private BigDecimal fee;
    
    @NotBlank(message = "Lecturer ID is required")
    private String lecturerId;
    
    @NotBlank(message = "Lecturer name is required")
    private String lecturerName;
    
    private Timestamp createdAt;
    private Timestamp updatedAt;
}