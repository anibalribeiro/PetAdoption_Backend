package com.ribeiroanibal.adopt.rest.data;

import org.springframework.data.domain.Page;

public class Pagination {

    private final Page page;
    private final int maxPageSize;

    public Pagination(final Page page, final int maxPageSize) {

        this.page = page;
        this.maxPageSize = maxPageSize;
    }

    public long getTotalRecords() {
        return page.getTotalElements();
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public int getCurrentPage() {
        return page.getNumber();
    }

    public int getPerPage() {
        return page.getSize();
    }

    public int getMaxPerPage() {
        return maxPageSize;
    }
}
