package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SearchListItemTest {


    @Test
    public void create() {
        String id = "entrance";
        String kind = "ball";
        String name = "aaaa";
        String tags = "sheesh";

        SearchListItem item = new SearchListItem(id, kind, name, tags);
        String item2  = "SearchListItem{" + "id='" + id + '\'' + ", kind='" + kind + "', name='" + name + '\'' + ", tags='" + tags + '\'' + '}';
        assertEquals(item2, item.toString());



    }

}
