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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        SearchListItem item1 = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
        SearchListItem item2 = new SearchListItem("gorillas", "exhibit", "Gorillas", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);

        assertNotEquals(id1, id2);
    }


    @Test
    public void testGet() {
        SearchListItem insertedItem = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
        dao.insert(insertedItem);
        String id = insertedItem.id;
        SearchListItem item = dao.get(id);
        assertEquals(id, item.id);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.tags, item.tags);
    }

    @Test
    public void testUpdate() {
        SearchListItem item = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
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
        SearchListItem item = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
        dao.insert(item);
        String id = item.id;

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));
    }

    @Test
    public void testReturnNames() {
        SearchListItem item = new SearchListItem("fentrance_exit_gate", "gate", "FEntrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
        dao.insert(item);
        String id1 = item.id;

        SearchListItem item2 = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
        dao.insert(item2);
        String id2 = item2.id;

        List<String> names = dao.getByName();
        List<String> correctNames = new ArrayList<>(Arrays.asList("Entrance and Exit Gate", "FEntrance and Exit Gate"));
        assertEquals(correctNames, names);
    }

    @Test
    public void testReturnNamesByTag() {
        SearchListItem item = new SearchListItem("fentrance_exit_gate", "gate", "FEntrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal", "area"})));
        dao.insert(item);

        SearchListItem item2 = new SearchListItem("entrance_exit_gate", "gate", "Entrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"animal"})));
        dao.insert(item2);

        SearchListItem item3 = new SearchListItem("hentrance_exit_gate", "gate", "HEntrance and Exit Gate", new ArrayList<String>(Arrays.asList(new String[]{"area"})));
        dao.insert(item3);

        List<String> names = dao.getByTag("area");
        List<String> correctNames = new ArrayList<String>(
                Arrays.asList("FEntrance and Exit Gate",
                        "HEntrance and Exit Gate"));
        assertEquals(correctNames, names);
    }

}
