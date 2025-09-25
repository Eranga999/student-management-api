package com.studentmanagement.api.controller;

import com.studentmanagement.api.dto.CourseRequestDto;
import com.studentmanagement.api.dto.CourseResponseDto;
import com.studentmanagement.api.dto.PageRequest;
import com.studentmanagement.api.dto.PageResponse;
import com.studentmanagement.api.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/courses")
    public ResponseEntity<CourseResponseDto> createCourse(@Valid @RequestBody CourseRequestDto courseRequestDto) {
        try {
            CourseResponseDto course = courseService.createCourse(courseRequestDto).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (Exception e) {
            log.error("Error creating course: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("Root cause: {}", e.getCause().getMessage());
            }
            log.error("Full stack trace", e);
            throw new RuntimeException("Failed to create course", e);
        }
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable String id) {
        try {
            CourseResponseDto course = courseService.getCourseById(id).get();
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            log.error("Error getting course: {}", e.getMessage());
            throw new RuntimeException("Failed to get course", e);
        }
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable String id, @Valid @RequestBody CourseRequestDto courseRequestDto) {
        try {
            CourseResponseDto course = courseService.updateCourse(id, courseRequestDto).get();
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            log.error("Error updating course: {}", e.getMessage());
            throw new RuntimeException("Failed to update course", e);
        }
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id).get();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting course: {}", e.getMessage());
            throw new RuntimeException("Failed to delete course", e);
        }
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        try {
            List<CourseResponseDto> courses = courseService.getAllCourses().get();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error getting courses: {}", e.getMessage());
            throw new RuntimeException("Failed to get courses", e);
        }
    }
    
    @GetMapping("/courses/paginated")
    public ResponseEntity<PageResponse<CourseResponseDto>> getAllCoursesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") PageRequest.SortDirection sortDirection) {
        try {
            PageRequest pageRequest = PageRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
                
            PageResponse<CourseResponseDto> pageResponse = courseService.getAllCoursesWithPagination(pageRequest).get();
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            log.error("Error getting courses with pagination: {}", e.getMessage());
            throw new RuntimeException("Failed to get courses with pagination", e);
        }
    }
}
