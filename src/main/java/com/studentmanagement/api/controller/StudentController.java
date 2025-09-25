package com.studentmanagement.api.controller;

import com.studentmanagement.api.dto.StudentRequestDto;
import com.studentmanagement.api.dto.StudentResponseDto;
import com.studentmanagement.api.dto.PageRequest;
import com.studentmanagement.api.dto.PageResponse;
import com.studentmanagement.api.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    
    private final StudentService studentService;
    
    @PostMapping("/student")
    public ResponseEntity<StudentResponseDto> createStudent(
            @Valid @RequestBody StudentRequestDto requestDto) {
        try {
            log.info("Creating student: {}", requestDto.getName());
            StudentResponseDto student = studentService.createStudent(requestDto).get();
            log.info("Student created successfully with ID: {}", student.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (Exception e) {
            // Log the actual exception for debugging
            log.error("Error creating student: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("Root cause: {}", e.getCause().getMessage());
            }
            log.error("Full stack trace", e);
            // Re-throw to trigger global exception handler
            throw new RuntimeException("Failed to create student", e);
        }
    }
    
    @GetMapping("/student/{id}")
    public CompletableFuture<ResponseEntity<StudentResponseDto>> getStudent(@PathVariable String id) {
        return studentService.getStudentById(id)
            .thenApply(student -> ResponseEntity.ok(student));
    }
    
    @GetMapping("/students")
    public CompletableFuture<ResponseEntity<List<StudentResponseDto>>> getAllStudents() {
        return studentService.getAllStudents()
            .thenApply(students -> ResponseEntity.ok(students));
    }
    
    @GetMapping("/students/paginated")
    public CompletableFuture<ResponseEntity<PageResponse<StudentResponseDto>>> getAllStudentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") PageRequest.SortDirection sortDirection) {
        
        PageRequest pageRequest = PageRequest.builder()
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
            
        return studentService.getAllStudentsWithPagination(pageRequest)
            .thenApply(pageResponse -> ResponseEntity.ok(pageResponse));
    }
    
    @PutMapping("/student/{id}")
    public CompletableFuture<ResponseEntity<StudentResponseDto>> updateStudent(
            @PathVariable String id, @Valid @RequestBody StudentRequestDto requestDto) {
        return studentService.updateStudent(id, requestDto)
            .thenApply(student -> ResponseEntity.ok(student));
    }
    
    @DeleteMapping("/student/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id)
            .thenApply(v -> ResponseEntity.noContent().build());
    }
}