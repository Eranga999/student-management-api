package com.studentmanagement.api.dto;
import lombok.Builder;
import lombok.Data;
import com.google.cloud.Timestamp;
@Data
@Builder
public class StudentResponseDto {
    private String id;
    private String title;
    private String name;
    private String address;
    private String city;
    private String course;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}