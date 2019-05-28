package com.ezgroceries.shoppinglist.cocktail.service;

import com.ezgroceries.shoppinglist.cocktail.entities.CocktailEntity;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;

import java.util.List;
import java.util.UUID;

public interface CocktailService {

    List<CocktailResource> getById(UUID id) ;

    List<CocktailResource> getAllById(List<UUID> ids) ;

    List<CocktailEntity> getAllEntitiesById(List<UUID> ids);

    List<CocktailResource> mergeCocktails(List<CocktailDBResponse.DrinkResource> drinks);
}
