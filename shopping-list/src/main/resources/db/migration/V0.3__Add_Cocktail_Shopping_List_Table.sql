create table COCKTAIL_SHOPPING_LIST (
                                      COCKTAIL_ID UUID REFERENCES cocktail (id),
                                      SHOPPING_LIST_ID UUID REFERENCES shopping_list (id)
);