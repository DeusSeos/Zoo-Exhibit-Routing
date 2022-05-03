package com.example.zooseeker;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SearchDatabaseTest2 {

    private SearchDatabase db;
    private SearchListDao dao;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SearchDatabase.class).allowMainThreadQueries().build();
        dao = db.searchListDao();
        List<SearchListItem> searchListItems = SearchListItem.loadJson(context, "sample_node_info.json");
        dao.insertAll(searchListItems);
    }

    @After
    public void close(){
        db.close();
    }

    @Test
    public void dead(){
        List<String> names = dao.getByName();
        System.out.print("Jlkasdf;klafjadffd");
        System.out.print(names.toString());
        assertTrue(names.contains("adjfadf"));


    }


}
