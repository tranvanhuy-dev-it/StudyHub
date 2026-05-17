package com.example.studyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> content;      // Dữ liệu trang hiện tại
    private long totalElements;   // Tổng số phần tử
    private int totalPages;       // Tổng số trang
    private int currentPage;      // Trang hiện tại
    private int pageSize;         // Kích thước trang

}