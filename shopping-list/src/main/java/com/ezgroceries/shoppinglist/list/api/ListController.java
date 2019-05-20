package com.ezgroceries.shoppinglist.list.api;
import com.ezgroceries.shoppinglist.list.model.ListResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ListController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ListResource> create(@RequestBody Map<String, String> body) {
        UUID id = UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915");
        ListResource shoppingList = new ListResource(id, body.get("name"));
        return entityWithLocation(id).body(shoppingList);
    }

    private ResponseEntity.BodyBuilder entityWithLocation(Object resourceId) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{childId}").buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location);
    }
}