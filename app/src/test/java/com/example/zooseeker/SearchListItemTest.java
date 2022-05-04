package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class SearchListItemTest {


    @Test
    public void create() {
        String id = "entrance";
        String kind = "ball";
        String name = "aaaa";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");

        SearchListItem item = new SearchListItem(id, kind, name, tags);
        String item2  = "SearchListItem{" + "id='" + id + '\'' + ", kind='" + kind + "', name='" + name + '\'' + ", tags='" + tags + '\'' + '}';
        assertEquals(item2, item.toString());



    }

}
