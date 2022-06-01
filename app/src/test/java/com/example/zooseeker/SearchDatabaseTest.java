package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseeker.db.Exhibit;
import com.example.zooseeker.db.ExhibitWithGroup;
import com.example.zooseeker.db.ExhibitsDao;
import com.example.zooseeker.db.SearchDatabase;

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


    private ExhibitsDao dao;
    private SearchDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SearchDatabase.class).allowMainThreadQueries().build();
        dao = db.exhibitsDao();

    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


    @Test
    public void testInsert() {
        String id = "entrance";
        Exhibit.Kind kind = Exhibit.Kind.GATE;
        String name = "aaaa";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");
        double lat = 40.0;
        double lng = 41.0;
        Exhibit newExhibit = new Exhibit(id, null, kind, name, tags, lat, lng);

        dao.insert(newExhibit);

        assertEquals(dao.count(), 1);
    }


    @Test
    public void testGet() {
        String id = "entrance";
        Exhibit.Kind kind = Exhibit.Kind.GATE;
        String name = "aaaa";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");
        double lat = 40.0;
        double lng = 41.0;
        Exhibit newExhibit = new Exhibit(id, null, kind, name, tags, lat, lng);
        dao.insert(newExhibit);


        ExhibitWithGroup item = dao.getExhibitById(id);
        assertEquals(newExhibit.name, item.getExhibitName());
        assertEquals(newExhibit.id, item.getExhibitID());
    }


    @Test
    public void testDelete() {
        String id = "entrance";
        Exhibit.Kind kind = Exhibit.Kind.GATE;
        String name = "aaaa";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");
        double lat = 40.0;
        double lng = 41.0;
        Exhibit newExhibit = new Exhibit(id, null, kind, name, tags, lat, lng);
        dao.insert(newExhibit);

        dao.deleteExhibits();
        assertEquals(0, dao.count());
    }


    @Test
    public void testReturnNames() {
        String id1 = "hue";
        String id2 = "huehue";
        Exhibit.Kind kind = Exhibit.Kind.GATE;
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");
        double lat = 40.0;
        double lng = 41.0;
        Exhibit newExhibit = new Exhibit(id1, null, kind, "Entrance and Exit Gate", tags, lat, lng);
        dao.insert(newExhibit);


        Exhibit newExhibit2 = new Exhibit(id2, null, kind, "FEntrance and Exit Gate", tags, lat, lng);
        dao.insert(newExhibit2);



        assertEquals(newExhibit2.name, "FEntrance and Exit Gate");
        assertEquals(newExhibit.name, "Entrance and Exit Gate");
    }



}
