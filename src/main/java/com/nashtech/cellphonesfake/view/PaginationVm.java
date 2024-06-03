package com.nashtech.cellphonesfake.view;

public record PaginationVm(
        int totalPages,
        Long totalElements,
        int size,
        int currentPage,
        Object content
) {
}
