package com.ezgroceries.shoppinglist.list.api;
import com.ezgroceries.shoppinglist.cocktail.api.CocktailController;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;
import com.ezgroceries.shoppinglist.list.model.ListResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ListController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ListResource> create(@RequestBody Map<String, String> body) {
        UUID id = UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915");
        ListResource shoppingList = new ListResource(id, body.get("name"), CocktailController.getDummyResources());
        return entityWithLocation(id).body(shoppingList);
    }

    @PostMapping(path = "/{id}/cocktails")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Map<String, UUID>>> addCocktails(@PathVariable String id, @RequestBody List<Map<String, UUID>> body) {
        ListResource shoplist = getList(id).getBody();
        //todo get cocktails form id and
        List<CocktailResource> cocktails = new ArrayList<>();
        shoplist.addCocktails(cocktails);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ListResource>> getList() {
        //get all shopping lists
        return ResponseEntity.ok(getDummyData());
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ListResource> getList(@PathVariable String id) {
        ListResource shoplist = getList(id).getBody();
        return ResponseEntity.ok(shoplist);
    }

    private ResponseEntity.BodyBuilder entityWithLocation(Object resourceId) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{childId}").buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location);
    }


    private List<ListResource> getDummyData() {
        return Arrays.asList(
                new ListResource(
                        UUID.fromString("4ba92a46-1d1b-4e52-8e38-13cd56c7224c"),
                        "Stephanie's Birthday",
                        CocktailController.getDummyResources()
                ),
                new ListResource(
                        UUID.fromString("6c7d09c2-8a25-4d54-a979-25ae779d2465"),
                        "My Birthday",
                        CocktailController.getDummyResources()
                )
        );
    }
}