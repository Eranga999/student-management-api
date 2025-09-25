package com.studentmanagement.api.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.studentmanagement.api.exception.StudentNotFoundException;
import com.studentmanagement.api.dto.PageRequest;
import com.studentmanagement.api.dto.PageResponse;
import com.studentmanagement.api.dto.StudentRequestDto;
import com.studentmanagement.api.dto.StudentResponseDto;
import com.studentmanagement.api.model.Student;
import com.studentmanagement.api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public CompletableFuture<StudentResponseDto> createStudent(StudentRequestDto requestDto) {
        log.info("Service: Creating student with name: {}", requestDto.getName());
        Student student = Student.builder()
            .title(requestDto.getTitle())
            .name(requestDto.getName())
            .address(requestDto.getAddress())
            .city(requestDto.getCity())
            .course(requestDto.getCourse())
            .build();
            
        log.debug("Service: Built student object, now saving...");
        return studentRepository.save(student)
            .thenCompose(id -> {
                log.debug("Service: Student saved with ID: {}, now fetching...", id);
                return studentRepository.findById(id);
            })
            .thenApply(savedStudent -> {
                log.debug("Service: Retrieved student, mapping to DTO...");
                return mapToResponseDto(savedStudent);
            });
    }
    
    public CompletableFuture<StudentResponseDto> getStudentById(String id) {
        return studentRepository.findById(id)
            .thenApply(student -> {
                if (student == null) {
                    throw new StudentNotFoundException("Student not found with id: " + id);
                }
                return mapToResponseDto(student);
            });
    }
    
    public CompletableFuture<List<StudentResponseDto>> getAllStudents() {
        return studentRepository.findAll()
            .thenApply(students -> students.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList()));
    }
    
    public CompletableFuture<PageResponse<StudentResponseDto>> getAllStudentsWithPagination(PageRequest pageRequest) {
        log.info("Service: Getting students with pagination - page: {}, size: {}", pageRequest.getPage(), pageRequest.getSize());
        
        CompletableFuture<List<Student>> studentsFuture = studentRepository.findAllWithPagination(pageRequest);
        CompletableFuture<Long> countFuture = studentRepository.count();
        
        return studentsFuture.thenCombine(countFuture, (students, totalCount) -> {
            List<StudentResponseDto> studentDtos = students.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
            
            return PageResponse.of(studentDtos, pageRequest, totalCount);
        });
    }
    
    public CompletableFuture<StudentResponseDto> updateStudent(String id, StudentRequestDto requestDto) {
        return studentRepository.findById(id)
            .thenCompose(existingStudent -> {
                if (existingStudent == null) {
                    throw new StudentNotFoundException("Student not found with id: " + id);
                }
                
                Student updatedStudent = Student.builder()
                    .id(id)
                    .title(requestDto.getTitle())
                    .name(requestDto.getName())
                    .address(requestDto.getAddress())
                    .city(requestDto.getCity())
                    .course(requestDto.getCourse())
                    .createdAt(existingStudent.getCreatedAt())
                    .build();
                    
                return studentRepository.save(updatedStudent);
            })
            .thenCompose(savedId -> studentRepository.findById(savedId))
            .thenApply(this::mapToResponseDto);
    }
    
    public CompletableFuture<Void> deleteStudent(String id) {
        return studentRepository.findById(id)
            .thenCompose(student -> {
                if (student == null) {
                    throw new StudentNotFoundException("Student not found with id: " + id);
                }
                return studentRepository.deleteById(id);
            });
    }
    
    private StudentResponseDto mapToResponseDto(Student student) {
        return StudentResponseDto.builder()
            .id(student.getId())
            .title(student.getTitle())
            .name(student.getName())
            .address(student.getAddress())
            .city(student.getCity())
            .course(student.getCourse())
            .createdAt(student.getCreatedAt())
            .updatedAt(student.getUpdatedAt())
            .build();
    }
}