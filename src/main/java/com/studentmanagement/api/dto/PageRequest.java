package com.studentmanagement.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    
    @Min(value = 0, message = "Page number must be 0 or greater")
    @Builder.Default
    private int page = 0;
    
    @Min(value = 1, message = "Page size must be 1 or greater")
    @Builder.Default
    private int size = 10;
    
    private String sortBy = "createdAt";
    
    @Builder.Default
    private SortDirection sortDirection = SortDirection.DESC;
    
    public enum SortDirection {
        ASC, DESC
    }
}