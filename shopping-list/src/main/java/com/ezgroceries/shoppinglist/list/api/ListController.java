package com.ezgroceries.shoppinglist.list.api;
import com.ezgroceries.shoppinglist.cocktail.api.CocktailController;
import com.ezgroceries.shoppinglist.cocktail.model.CocktailResource;
import com.ezgroceries.shoppinglist.list.model.ListResource;
import com.ezgroceries.shoppinglist.list.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ListResource> create(@RequestBody Map<String, String> body) {
        ListResource shoppingList = this.listService.create(body.get("name"));
        return entityWithLocation(shoppingList.getShoppingListId()).body(shoppingList);
    }

    @PostMapping(path = "/{id}/cocktails")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Map<String, UUID>>> addCocktails(@PathVariable String id, @RequestBody List<Map<String, UUID>> body) {
        List<UUID> cocktails = body.stream().map(map -> map.get("cocktailId")).collect(Collectors.toList());
        this.listService.addCocktails(UUID.fromString(id), cocktails);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ListResource>> getList() {
        return ResponseEntity.ok(this.listService.getAll());
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
}