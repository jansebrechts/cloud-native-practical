package com.ezgroceries.shoppinglist.list.model;

import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListResource {
    private final UUID shoppingListId;
    private final String name;
    private final List<String> ingredients;

    public ListResource(UUID shoppingListId, String name, List<CocktailResource> cocktails) {
        this.shoppingListId = shoppingListId;
        this.name = name;
        ingredients = new ArrayList<String>();
        addCocktails(cocktails);
    }

    public UUID getShoppingListId() {
        return shoppingListId;
    }

    public String getName() {
        return name;
    }

    public void addCocktails(List<CocktailResource> cocktails) {
        this.ingredients.addAll(cocktails.stream()
                .flatMap(el -> el.getIngredients().stream())
                .distinct()
                .collect(Collectors.toList()));
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
