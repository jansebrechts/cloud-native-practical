package com.ezgroceries.shoppinglist.cocktail.repository;

import com.ezgroceries.shoppinglist.cocktail.entities.CocktailEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CocktailRepository extends CrudRepository<CocktailEntity, UUID> {
    List<CocktailEntity> findByIdDrink(List<String> ids);
    List<CocktailEntity> findAllById(List<UUID> ids);
}
