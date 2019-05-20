package com.ezgroceries.shoppinglist.list.model;

import java.util.UUID;

public class ListResource {
    private final UUID shoppingListId;
    private final String name;

    public ListResource(UUID shoppingListId, String name) {
        this.shoppingListId = shoppingListId;
        this.name = name;
    }

    public UUID getShoppingListId() {
        return shoppingListId;
    }

    public String getName() {
        return name;
    }
}
