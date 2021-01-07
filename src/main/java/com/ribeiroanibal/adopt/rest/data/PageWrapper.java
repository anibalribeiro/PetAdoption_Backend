package com.ribeiroanibal.adopt.rest.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Implements Page interface and wrap into the way I want to display
 */
@JsonPropertyOrder({"data", "pagination"})
public class PageWrapper<T> implements Page<T> {
    private final Page<T> page;
    private final int maxPageSize;

    public PageWrapper(final Page<T> page, final int maxPageSize) {
        this.page = page;
        this.maxPageSize = maxPageSize;
    }

    @JsonIgnore
    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @JsonIgnore
    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @JsonIgnore
    @Override
    public <U> Page<U> map(final Function<? super T, ? extends U> converter) {
        return page.map(converter);
    }

    @JsonIgnore
    @Override
    public int getNumber() {
        return page.getNumber();
    }

    @JsonIgnore
    @Override
    public int getSize() {
        return page.getSize();
    }

    @JsonIgnore
    @Override
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    @JsonProperty("data")
    @Override
    public List<T> getContent() {
        return page.getContent();
    }

    @JsonIgnore
    @Override
    public boolean hasContent() {
        return page.hasContent();
    }

    @JsonIgnore
    @Override
    public Sort getSort() {
        return page.getSort();
    }

    @JsonIgnore
    @Override
    public boolean isFirst() {
        return page.isFirst();
    }

    @JsonIgnore
    @Override
    public boolean isLast() {
        return page.isLast();
    }

    @JsonIgnore
    @Override
    public boolean hasNext() {
        return page.hasNext();
    }

    @JsonIgnore
    @Override
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    @JsonIgnore
    @Override
    public Pageable nextPageable() {
        return page.nextPageable();
    }

    @JsonIgnore
    @Override
    public Pageable previousPageable() {
        return page.previousPageable();
    }

    @JsonIgnore
    @Override
    public Pageable getPageable() {
        return page.getPageable();
    }

    @JsonIgnore
    @Override
    public Iterator<T> iterator() {
        return page.iterator();
    }

    public Pagination getPagination() {
        return new Pagination(page, maxPageSize);
    }
}
