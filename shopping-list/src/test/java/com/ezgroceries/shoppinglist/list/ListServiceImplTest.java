package com.ezgroceries.shoppinglist.list;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezgroceries.shoppinglist.cocktail.service.CocktailServiceImpl;
import com.ezgroceries.shoppinglist.list.entities.ListEntity;
import com.ezgroceries.shoppinglist.list.model.ListResource;
import com.ezgroceries.shoppinglist.list.repository.ListRepository;
import com.ezgroceries.shoppinglist.list.service.ListService;
import com.ezgroceries.shoppinglist.list.service.ListServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ListServiceImplTest {

    private ListService service;
    private ListRepository repository = mock(ListRepository.class);
    private CocktailServiceImpl cocktailService = mock(CocktailServiceImpl.class);

    @BeforeEach
    public void init() {
        service = new ListServiceImpl(repository, cocktailService);
    }

    @Test
    public void testCreateList() {
        when(repository.save(any(ListEntity.class))).thenAnswer(a -> a.getArgument(0));
        ArgumentCaptor<ListEntity> argumentCaptor = ArgumentCaptor.forClass(ListEntity.class);

        ListResource created = service.create("cool-list");
        assertThat("Name has been set", created.getName(), equalTo("cool-list"));

        verify(repository).save(argumentCaptor.capture());

        assertThat("Random id is created", created.getShoppingListId(), equalTo(argumentCaptor.getValue().getId()));
    }

    @Test
    public void testGet() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(of(new ListEntity("cool-list")));
        ListResource shoppingList = service.get(id.toString());
        assertThat("Shopping list exists", shoppingList, notNullValue());
        assertThat("Name has been set", shoppingList.getName(), equalTo("cool-list"));
    }


    @Test
    public void testGetLists() {
        when(repository.findAll()).thenReturn(Arrays.asList(new ListEntity("cool-drink-1"), new ListEntity("cool-drink-2")));
        List<ListResource> shoppingLists = service.getAll();
        assertThat("Shopping lists length is 2", shoppingLists.size(), equalTo(2));
        assertThat("Shopping list 1 is cool-drink-1", shoppingLists.get(0).getName(), equalTo("cool-drink-1"));
        assertThat("Shopping list 2 is cool-drink-2", shoppingLists.get(1).getName(), equalTo("cool-drink-2"));
    }
}
