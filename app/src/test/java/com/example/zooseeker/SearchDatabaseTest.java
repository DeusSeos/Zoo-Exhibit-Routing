package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SearchDatabaseTest {

    private SearchListDao dao;
    private SearchDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SearchDatabase.class).allowMainThreadQueries().build();
        dao = db.searchListDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


    @Test
    public void testInsert() {
        SearchListItem item1 = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", "[\"animal\"]");
        SearchListItem item2 = new SearchListItem("gorillas", "exhibit", "Gorillas","[\"animal\"]");

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);

        assertNotEquals(id1, id2);
    }


    @Test
    public void testGet() {
        SearchListItem insertedItem = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", "[\"animal\"]");
        dao.insert(insertedItem);
        String id = insertedItem.id;
        SearchListItem item = dao.get(id);
        assertEquals(id, item.id);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.tags, item.tags);
    }

    @Test
    public void testUpdate() {
        SearchListItem item = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", "[\"animal\"]");
        dao.insert(item);
        String id = item.id;

        item = dao.get(id);
        item.name = "exhibit";
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals("exhibit", item.name);

    }

    @Test
    public void testDelete() {
        SearchListItem item = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", "[\"animal\"]");
        dao.insert(item);
        String id = item.id;

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));
    }

}
