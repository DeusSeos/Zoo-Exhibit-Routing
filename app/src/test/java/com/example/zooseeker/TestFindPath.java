package com.example.zooseeker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestFindPath {

//    @Before
//    public void createContext(){
//        Context context = ApplicationProvider.getApplicationContext();
//
//    }

    @Test
    public void testOnePlace(){
        Context context = ApplicationProvider.getApplicationContext();

        SearchListItem item = new SearchListItem("gorillas", "exhibit", "Gorillas",
                new ArrayList<String>(Arrays.asList("gorilla", "monkey", "ape", "mammal")));
        List<SearchListItem> selected = Arrays.asList(item);

        Pathfinder finder = new Pathfinder(context, selected);
        finder.optimizeSelectedItemsIDs();
        ArrayList<String> first = new ArrayList<>(Arrays.asList("  1. Walk 10 meters along Entrance Way between \'Entrance and Exit Gate\' and \'Entrance Plaza\'.\n","  2. Walk 200 meters along Africa Rocks Street between \'Entrance Plaza\' and \'Gorillas\'.\n"));
        assertEquals(first, finder.next());
        ArrayList<String> second = new ArrayList<>(Arrays.asList("  1. Walk 200 meters along Africa Rocks Street between 'Entrance Plaza' and 'Gorillas'.\n", "  2. Walk 10 meters along Entrance Way between 'Entrance and Exit Gate' and 'Entrance Plaza'.\n"));
        assertEquals(second, finder.next());
        ArrayList<String> last = new ArrayList<>(Arrays.asList("No more"));
        assertEquals(second, finder.next());
    }

    @Test
    public void testMultiplePlaced(){

    }
}
