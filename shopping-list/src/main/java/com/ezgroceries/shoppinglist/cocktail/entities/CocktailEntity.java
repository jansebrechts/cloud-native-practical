package com.ezgroceries.shoppinglist.cocktail.entities;

import com.ezgroceries.shoppinglist.util.StringSetConverter;

import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "COCKTAIL")
public class CocktailEntity {

    @Id
    private UUID id;

    @Column(name="id_drink")
    private String idDrink;

    private String name;

    @Convert(converter = StringSetConverter.class)
    private Set<String> ingredients;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(String idDrink) {
        this.idDrink = idDrink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }
}
