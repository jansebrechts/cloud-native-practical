package com.ezgroceries.shoppinglist.list.service;

import com.ezgroceries.shoppinglist.list.exceptions.ListException;
import com.ezgroceries.shoppinglist.list.model.ListResource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface ListService {

    ListResource create(String name);

    ListResource addCocktails(UUID listId, List<UUID> cocktailIds) throws ListException;

    ListResource get(String uuid);

    List<ListResource> getAll();
}