package com.studentmanagement.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public static <T> PageResponse<T> of(List<T> content, PageRequest pageRequest, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageRequest.getSize());
        boolean isFirst = pageRequest.getPage() == 0;
        boolean isLast = pageRequest.getPage() >= totalPages - 1;
        
        return PageResponse.<T>builder()
                .content(content)
                .currentPage(pageRequest.getPage())
                .pageSize(pageRequest.getSize())
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(isFirst)
                .last(isLast)
                .hasNext(!isLast && totalElements > 0)
                .hasPrevious(!isFirst)
                .build();
    }
}