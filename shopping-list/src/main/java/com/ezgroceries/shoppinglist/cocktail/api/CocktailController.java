package com.ezgroceries.shoppinglist.cocktail.api;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;
import com.ezgroceries.shoppinglist.cocktail.service.CocktailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private final CocktailServiceImpl cocktailService;

    public CocktailController(CocktailServiceImpl cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public ResponseEntity<List<CocktailResource>> get(@RequestParam String search) {
        return ResponseEntity.ok(cocktailService.getById(UUID.fromString(search)));
    }

}