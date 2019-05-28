package com.ezgroceries.shoppinglist.list.service;

import com.ezgroceries.shoppinglist.cocktail.entities.CocktailEntity;
import com.ezgroceries.shoppinglist.cocktail.service.CocktailServiceImpl;
import com.ezgroceries.shoppinglist.list.entities.ListEntity;
import com.ezgroceries.shoppinglist.list.exceptions.ListException;
import com.ezgroceries.shoppinglist.list.model.ListResource;
import com.ezgroceries.shoppinglist.list.repository.ListRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListServiceImpl implements ListService {

    private final ListRepository shoppingListRepository;
    private final CocktailServiceImpl cocktailService;

    public ListServiceImpl(ListRepository shoppingListRepository, CocktailServiceImpl cocktailService) {
        this.shoppingListRepository = shoppingListRepository;
        this.cocktailService = cocktailService;
    }

    public ListResource create(String name) {
        ListEntity entity = new ListEntity(name);
        entity = shoppingListRepository.save(entity);
        ListResource resource = convertToResource(entity);
        return resource;
    }

    public ListResource addCocktails(UUID listId, List<UUID> cocktailIds) throws ListException {
        List<CocktailEntity> cocktailEntities = cocktailService.getAllEntitiesById(cocktailIds);
        return shoppingListRepository.findById(listId).map(shoppingList -> {
            shoppingList.addCocktails(cocktailEntities);
            shoppingListRepository.save(shoppingList);
            return convertToResource(shoppingList);
        }).orElseThrow(() -> new ListException("Shopping list with id '" + listId + "' not found"));
    }

    public ListResource get(String uuid) {
        Optional<ListEntity> entity = shoppingListRepository.findById(UUID.fromString(uuid));
        return entity.map(this::convertToResource).orElse(null);
    }

    public List<ListResource> getAll() {
        List<ListEntity> entity = shoppingListRepository.findAll();
        return entity.stream().map(this::fromListEntityWithIngredients).collect(Collectors.toList());
    }

    private ListResource convertToResource(ListEntity entity){
        ListResource resource = new ListResource(entity.getId(), entity.getName());
        return resource;
    }

    private ListResource fromListEntityWithIngredients(ListEntity entity){
        ListResource shoppingList = convertToResource(entity);
        List<CocktailEntity> entities = (entity.getCocktails() != null) ? entity.getCocktails() : new ArrayList<>();
        List<UUID> ids = entities.stream().map(CocktailEntity::getId).collect(Collectors.toList());
        List<String> ingredients = cocktailService.getAllEntitiesById(ids).stream().map(CocktailEntity::getIngredients).flatMap(Set::stream).distinct().collect(Collectors.toList());
        shoppingList.setIngredients(ingredients);
        return shoppingList;
    }
}