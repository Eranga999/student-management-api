package com.studentmanagement.api.dto;
import lombok.Builder;
import lombok.Data;
import com.google.cloud.Timestamp;

@Data
@Builder
public class CourseResponseDto {
    private String id;
    private String name;
    private String fee;
    private String lecturerId;
    private String lecturerName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

