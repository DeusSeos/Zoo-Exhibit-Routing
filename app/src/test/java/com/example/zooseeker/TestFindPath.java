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

    /*
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
        assertEquals(last, finder.next());
    }

    @Test
    public void testMultiplePlaced(){
        Context context = ApplicationProvider.getApplicationContext();

        SearchListItem item1 = new SearchListItem("gorillas", "exhibit", "Gorillas",
                new ArrayList<String>(Arrays.asList("gorilla", "monkey", "ape", "mammal")));

        SearchListItem item2 = new SearchListItem("gators", "exhibit", "Alligators",
                new ArrayList<String>(Arrays.asList("alligator", "reptile", "gator")));

        SearchListItem item3 = new SearchListItem("lions", "exhibit", "Lions",
                new ArrayList<String>(Arrays.asList("lions", "cats", "mammal", "africa")));

        List<SearchListItem> selected = Arrays.asList(item1, item2, item3);
        Pathfinder finder = new Pathfinder(context, selected);
        finder.optimizeSelectedItemsIDs();
        ArrayList<String> first = new ArrayList<>(Arrays.asList("  1. Walk 10 meters along Entrance Way between 'Entrance and Exit Gate' and 'Entrance Plaza'.\n",
                "  2. Walk 200 meters along Africa Rocks Street between 'Entrance Plaza' and 'Gorillas'.\n"));

        assertEquals(first, finder.next());
        ArrayList<String> second = new ArrayList<>(Arrays.asList("  1. Walk 200 meters along Africa Rocks Street between 'Entrance Plaza' and 'Gorillas'.\n",
                "  2. Walk 100 meters along Reptile Road between 'Entrance Plaza' and 'Alligators'.\n"));
        assertEquals(second, finder.next());
        ArrayList<String> third = new ArrayList<>(Arrays.asList("  1. Walk 200 meters along Sharp Teeth Shortcut between 'Alligators' and 'Lions'.\n"));
        assertEquals(third, finder.next());
        ArrayList<String> fourth = new ArrayList<>(Arrays.asList("  1. Walk 200 meters along Sharp Teeth Shortcut between 'Alligators' and 'Lions'.\n", "  2. Walk 100 meters along Reptile Road between 'Entrance Plaza' and 'Alligators'.\n", "  3. Walk 10 meters along Entrance Way between 'Entrance and Exit Gate' and 'Entrance Plaza'.\n"));
        assertEquals(fourth, finder.next());
        ArrayList<String> last = new ArrayList<>(Arrays.asList("No more"));
        assertEquals(last, finder.next());
    }
    */

}
