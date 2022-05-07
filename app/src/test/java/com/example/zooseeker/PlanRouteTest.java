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
public class PlanRouteTest {

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
    public void testOnePlace(){
        // Test plan with only one attraction
    }

    @Test
    public void testManyPlaces(){
        // Test plane with more than two attractions
    }
}
