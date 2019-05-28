package com.ezgroceries.shoppinglist.list;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezgroceries.shoppinglist.list.model.ListResource;
import com.ezgroceries.shoppinglist.list.service.ListService;
import com.ezgroceries.shoppinglist.list.service.ListServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListService shoppingListService;

    @Test
    public void createListResource() throws Exception {
        given(shoppingListService.create("Stephanie's birthday")).willReturn(getListResourceMock());
        mockMvc
                .perform(
                        post("/shopping-lists")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"Stephanie's birthday\"}")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("shoppingListId").value("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"))
                .andExpect(jsonPath("name").value("Stephanie's birthday"));
        verify(shoppingListService).create("Stephanie's birthday");
    }

    @Test
    public void addCocktailsToList() throws Exception {
        mockMvc
                .perform(
                        post("/shopping-lists/97c8e5bd-5353-426e-b57b-69eb2260ace3/cocktails")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("[{\"cocktailId\": \"23b3d85a-3928-41c0-a533-6538a71e17c4\"}, {\"cocktailId\": \"d615ec78-fe93-467b-8d26-5d26d8eab073\"}]")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].cocktailId").value("23b3d85a-3928-41c0-a533-6538a71e17c4"));
    }

    @Test
    public void getListResource() throws Exception {
        given(shoppingListService.get("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")).willReturn(getListResourceWithIngredientsMock());
        mockMvc
                .perform(
                        get("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.shoppingListId").value("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"))
                .andExpect(jsonPath("$.name").value("Stephanie's birthday"))
                .andExpect(jsonPath("$.ingredients.length()").value(5))
                .andExpect(jsonPath("$.ingredients[0]").value("Tequila"))
                .andExpect(jsonPath("$.ingredients[1]").value("Triple sec"))
                .andExpect(jsonPath("$.ingredients[2]").value("Lime juice"))
                .andExpect(jsonPath("$.ingredients[3]").value("Salt"))
                .andExpect(jsonPath("$.ingredients[4]").value("Blue Curacao"));
        verify(shoppingListService).get("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915");
    }

    @Test
    public void getListResources() throws Exception {
        given(shoppingListService.getAll()).willReturn(getListResourcesMock());
        mockMvc
                .perform(
                        get("/shopping-lists")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shoppingListId").value("4ba92a46-1d1b-4e52-8e38-13cd56c7224c"))
                .andExpect(jsonPath("$[0].name").value("Stephanie's birthday"))
                .andExpect(jsonPath("$[0].ingredients.length()").value(5))
                .andExpect(jsonPath("$[0].ingredients[0]").value("Tequila"))
                .andExpect(jsonPath("$[0].ingredients[1]").value("Triple sec"))
                .andExpect(jsonPath("$[0].ingredients[2]").value("Lime juice"))
                .andExpect(jsonPath("$[0].ingredients[3]").value("Salt"))
                .andExpect(jsonPath("$[0].ingredients[4]").value("Blue Curacao"))
                .andExpect(jsonPath("$[1].shoppingListId").value("6c7d09c2-8a25-4d54-a979-25ae779d2465"))
                .andExpect(jsonPath("$[1].name").value("My birthday"))
                .andExpect(jsonPath("$[1].ingredients.length()").value(5))
                .andExpect(jsonPath("$[1].ingredients[0]").value("Tequila"))
                .andExpect(jsonPath("$[1].ingredients[1]").value("Triple sec"))
                .andExpect(jsonPath("$[1].ingredients[2]").value("Lime juice"))
                .andExpect(jsonPath("$[1].ingredients[3]").value("Salt"))
                .andExpect(jsonPath("$[1].ingredients[4]").value("Blue Curacao"));
        verify(shoppingListService).getAll();
    }

    private ListResource getListResourceMock() {
        return new ListResource(UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"), "Stephanie's birthday");
    }

    private ListResource getListResourceWithIngredientsMock() {
        ListResource shoppingList = getListResourceMock();
        Arrays.asList("Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                "Blue Curacao").forEach(shoppingList::addIngredient);
        return shoppingList;
    }

    private List<ListResource> getListResourcesMock() {
        List<ListResource> lists = new ArrayList<>();
        ListResource shoppingList = new ListResource(UUID.fromString("4ba92a46-1d1b-4e52-8e38-13cd56c7224c"), "Stephanie's birthday");
        Arrays.asList("Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                "Blue Curacao").forEach(shoppingList::addIngredient);
        lists.add(shoppingList);
        shoppingList = new ListResource(UUID.fromString("6c7d09c2-8a25-4d54-a979-25ae779d2465"), "My birthday");
        Arrays.asList("Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                "Blue Curacao").forEach(shoppingList::addIngredient);
        lists.add(shoppingList);
        return lists;
    }
}
