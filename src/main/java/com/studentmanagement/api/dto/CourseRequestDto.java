package com.studentmanagement.api.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {
    
    @NotBlank(message = "Course name is required")
    private String name;

    @NotBlank(message = "Fee is required")
    private String fee;
    
    @NotBlank(message = "Lecturer ID is required")
    private String lecturerId;
    
    @NotBlank(message = "Lecturer name is required")    
    private String lecturerName;
}
