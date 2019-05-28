package com.ezgroceries.shoppinglist.list.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListResource {
    private UUID shoppingListId;
    private String name;
    private List<String> ingredients;

    public ListResource(UUID listId, String name) {
        this.shoppingListId = listId;
        this.name = name;
    }

    public UUID getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(UUID shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient) {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        ingredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
