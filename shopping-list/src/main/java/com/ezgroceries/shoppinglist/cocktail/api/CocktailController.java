package com.ezgroceries.shoppinglist.cocktail.api;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailDBClient;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private CocktailDBClient cocktailDBClient;

    @Autowired
    public void setCocktailDBClient(CocktailDBClient cocktailDBClient) {
        this.cocktailDBClient = cocktailDBClient;
    }


    private List<CocktailResource> transform(CocktailDBResponse dbResponse) {
        return dbResponse.getDrinks().stream()
                .map(drinkResource -> new CocktailResource(
                                UUID.randomUUID(),
                                drinkResource.getStrDrink(),
                                drinkResource.getStrGlass(),
                                drinkResource.getStrInstructions(),
                                drinkResource.getStrDrinkThumb(),
                                Stream.of(
                                        drinkResource.getStrIngredient1(),
                                        drinkResource.getStrIngredient2(),
                                        drinkResource.getStrIngredient3(),
                                        drinkResource.getStrIngredient4(),
                                        drinkResource.getStrIngredient5()
                                ).filter(StringUtils::isNotBlank).collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());

    }

    @GetMapping
    public ResponseEntity<List<CocktailResource>>  get(@RequestParam String search) {
        CocktailDBResponse cocktailDBResponse = cocktailDBClient.searchCocktails(search);
        return ResponseEntity.ok(transform(cocktailDBResponse));
    }

    public static List<CocktailResource> getDummyResources() {
        return Arrays.asList(
                new CocktailResource(
                        UUID.fromString("23b3d85a-3928-41c0-a533-6538a71e17c4"), "Margerita",
                        "Cocktail glass",
                        "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..",
                        "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg",
                        Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt")),
                new CocktailResource(
                        UUID.fromString("d615ec78-fe93-467b-8d26-5d26d8eab073"), "Blue Margerita",
                        "Cocktail glass",
                        "Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..",
                        "https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg",
                        Arrays.asList("Tequila", "Blue Curacao", "Lime juice", "Salt")));
    }
}