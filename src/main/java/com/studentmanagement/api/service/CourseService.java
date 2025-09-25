package com.studentmanagement.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.studentmanagement.api.dto.CourseRequestDto;
import com.studentmanagement.api.dto.CourseResponseDto;
import com.studentmanagement.api.dto.PageRequest;
import com.studentmanagement.api.dto.PageResponse;
import com.studentmanagement.api.exception.CourseNotFoundException;
import com.studentmanagement.api.model.Course;
import com.studentmanagement.api.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    // Create a new course
    public CompletableFuture<CourseResponseDto> createCourse(CourseRequestDto requestDto) {
        Course course = Course.builder()
                .name(requestDto.getName())
                .fee(new BigDecimal(requestDto.getFee())) // convert String to BigDecimal
                .lecturerId(requestDto.getLecturerId())
                .lecturerName(requestDto.getLecturerName())
                .build();

        return courseRepository.save(course)
                .thenCompose(id -> courseRepository.findById(id))
                .thenApply(this::mapToResponseDto);
    }

    // Get a course by ID
    public CompletableFuture<CourseResponseDto> getCourseById(String id) {
        return courseRepository.findById(id)
                .thenApply(course -> {
                    if (course == null) {
                        throw new CourseNotFoundException("Course not found with id: " + id);
                    }
                    return mapToResponseDto(course);
                });
    }

    // Get all courses
    public CompletableFuture<List<CourseResponseDto>> getAllCourses() {
        return courseRepository.findAll()
                .thenApply(courses -> courses.stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList()));
    }
    
    // Get all courses with pagination
    public CompletableFuture<PageResponse<CourseResponseDto>> getAllCoursesWithPagination(PageRequest pageRequest) {
        log.info("Service: Getting courses with pagination - page: {}, size: {}", pageRequest.getPage(), pageRequest.getSize());
        
        CompletableFuture<List<Course>> coursesFuture = courseRepository.findAllWithPagination(pageRequest);
        CompletableFuture<Long> countFuture = courseRepository.count();
        
        return coursesFuture.thenCombine(countFuture, (courses, totalCount) -> {
            List<CourseResponseDto> courseDtos = courses.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
            
            return PageResponse.of(courseDtos, pageRequest, totalCount);
        });
    }

    // Update an existing course
    public CompletableFuture<CourseResponseDto> updateCourse(String id, CourseRequestDto requestDto) {
        return courseRepository.findById(id)
                .thenCompose(existingCourse -> {
                    if (existingCourse == null) {
                        throw new CourseNotFoundException("Course not found with id: " + id);
                    }

                    Course updatedCourse = Course.builder()
                            .id(id)
                            .name(requestDto.getName())
                            .fee(new BigDecimal(requestDto.getFee())) // convert String to BigDecimal
                            .lecturerId(requestDto.getLecturerId())
                            .lecturerName(requestDto.getLecturerName())
                            .createdAt(existingCourse.getCreatedAt())
                            .build();

                    return courseRepository.save(updatedCourse);
                })
                .thenCompose(savedId -> courseRepository.findById(savedId))
                .thenApply(this::mapToResponseDto);
    }

    // Delete a course
    public CompletableFuture<Void> deleteCourse(String id) {
        return courseRepository.findById(id)
                .thenCompose(existingCourse -> {
                    if (existingCourse == null) {
                        throw new CourseNotFoundException("Course not found with id: " + id);
                    }
                    return courseRepository.deleteById(id);
                });
    }

    // Map Course entity to CourseResponseDto
    private CourseResponseDto mapToResponseDto(Course course) {
        return CourseResponseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .fee(course.getFee().toString())
                .lecturerId(course.getLecturerId())
                .lecturerName(course.getLecturerName())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
