package com.ribeiroanibal.adopt.repository.base;

import com.ribeiroanibal.adopt.model.BaseEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Base repository class extending PagingAndSortingRepository
 * so I can get findAll(Pageable pageable) and findAll(Sort sort) methods for paging and sorting.
 */
public interface BaseRepository<E extends BaseEntity, ID extends Serializable> extends PagingAndSortingRepository<E,
        ID> {
}
