package com.example.zooseeker;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseeker.db.Exhibit;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ExhibitTest {
    @Test
    public void create() {
        String id = "entrance";
        Exhibit.Kind kind = Exhibit.Kind.GATE;
        String name = "aaaa";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");
        double lat = 40.0;
        double lng = 41.0;

        Exhibit newExhibit = new Exhibit(id, null, kind, name, tags, lat, lng);
        assertEquals("Exhibit{id='entrance', groupId='null', kind=GATE, name='aaaa', tags=[sheesh], lat=40.0, lng=41.0}", newExhibit.toString());

    }

    @Test
    public void createWithGroup(){
        String id = "entrance";
        String groupId = "daGroup";        Exhibit.Kind kind = Exhibit.Kind.GATE;

        String name = "aaaa";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sheesh");
        double lat = 40.0;
        double lng = 41.0;

        Exhibit newExhibit = new Exhibit(id, groupId, kind, name, tags, lat, lng);
        assertEquals("Exhibit{id='entrance', groupId='daGroup', kind=GATE, name='aaaa', tags=[sheesh], lat=40.0, lng=41.0}", newExhibit.toString());
    }
}