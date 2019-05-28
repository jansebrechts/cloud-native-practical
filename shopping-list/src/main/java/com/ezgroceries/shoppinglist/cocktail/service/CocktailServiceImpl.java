package com.ezgroceries.shoppinglist.cocktail.service;

import com.ezgroceries.shoppinglist.cocktail.entities.CocktailEntity;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailDBClient;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;
import com.ezgroceries.shoppinglist.cocktail.repository.CocktailRepository;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CocktailServiceImpl implements CocktailService {
    private final CocktailRepository cocktailRepository;

    private final CocktailDBClient cocktailDBClient;

    public CocktailServiceImpl(CocktailRepository cocktailRepository, CocktailDBClient cocktailDBClient) {
        this.cocktailRepository = cocktailRepository;
        this.cocktailDBClient = cocktailDBClient;
    }

    public List<CocktailResource> getById(UUID id) {
        return mergeCocktails(cocktailDBClient.searchCocktails(id.toString()).getDrinks());
    }

    public List<CocktailResource> getAllById(List<UUID> ids) {
        List<CocktailResource> list = new ArrayList<CocktailResource>();
        for(UUID id : ids){
            list.addAll(this.getById(id));
        }
        return list;
    }

    public List<CocktailEntity> getAllEntitiesById(List<UUID> ids) {
        return cocktailRepository.findAllById(ids);
    }

    public List<CocktailResource> mergeCocktails(List<CocktailDBResponse.DrinkResource> drinks) {
        //Get all the idDrink attributes
        List<String> ids = drinks.stream().map(CocktailDBResponse.DrinkResource::getIdDrink).collect(Collectors.toList());

        //Get all the ones we already have from our DB, use a Map for convenient lookup
        Map<String, CocktailEntity> existingEntityMap = cocktailRepository.findByIdDrink(ids).stream().collect(Collectors.toMap(CocktailEntity::getIdDrink, o -> o, (o, o2) -> o));

        //Stream over all the drinks, map them to the existing ones, persist a new one if not existing
        Map<String, CocktailEntity> allEntityMap = drinks.stream().map(drinkResource -> {
            CocktailEntity cocktailEntity = existingEntityMap.get(drinkResource.getIdDrink());
            if (cocktailEntity == null) {
                CocktailEntity newCocktailEntity = new CocktailEntity();
                newCocktailEntity.setId(UUID.randomUUID());
                newCocktailEntity.setIdDrink(drinkResource.getIdDrink());
                newCocktailEntity.setName(drinkResource.getStrDrink());
                cocktailEntity = cocktailRepository.save(newCocktailEntity);
            }
            return cocktailEntity;
        }).collect(Collectors.toMap(CocktailEntity::getIdDrink, o -> o, (o, o2) -> o));

        //Merge drinks and our entities, transform to CocktailResource instances
        return mergeAndTransform(drinks, allEntityMap);
    }

    private List<CocktailResource> mergeAndTransform(List<CocktailDBResponse.DrinkResource> drinks, Map<String, CocktailEntity> allEntityMap) {
        return drinks.stream().map(drinkResource -> new CocktailResource(allEntityMap.get(drinkResource.getIdDrink()).getId(), drinkResource.getStrDrink(), drinkResource.getStrGlass(),
                drinkResource.getStrInstructions(), drinkResource.getStrDrinkThumb(), getIngredients(drinkResource))).collect(Collectors.toList());
    }

    private List<String> getIngredients(CocktailDBResponse.DrinkResource drinkResource) {
        return Stream.of(
                drinkResource.getStrIngredient1(),
                drinkResource.getStrIngredient2(),
                drinkResource.getStrIngredient3(),
                drinkResource.getStrIngredient4(),
                drinkResource.getStrIngredient5()
        ).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
