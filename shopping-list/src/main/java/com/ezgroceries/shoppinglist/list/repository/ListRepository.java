package com.ezgroceries.shoppinglist.list.repository;

import com.ezgroceries.shoppinglist.list.entities.ListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListRepository extends CrudRepository<ListEntity, UUID> {
    List<ListEntity> findAll();
    Optional<ListEntity> findById(UUID id);
}
