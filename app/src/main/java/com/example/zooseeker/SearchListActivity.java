package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchListActivity extends AppCompatActivity {

    //Declared variables
    Button planButton;
    TextView selectedCount;
    ListView listView;
    ListView selectedListView;
    SearchView searchView;
    SearchListItemAdapter adapter;
    SelectedListAdapter selectedAdapter;

    ExhibitsDao exhDao;
    List<ExhibitWithGroup> animalNameList;
    LinkedHashSet<Exhibit> selectedItems = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // initialize db stuff
        SearchDatabase db = SearchDatabase.getSingleton(this);
        exhDao = db.exhibitsDao();


        // load database if empty
        if (exhDao.count() == 0) {
            Reader exhibitsReader = null;
            Reader trailsReader = null;
            try {
                exhibitsReader = new InputStreamReader(getAssets().open("exhibit_info.json"));
                trailsReader = new InputStreamReader(getAssets().open("trail_info.json"));
            } catch (IOException e) {
                throw new RuntimeException("Unable to load data for prepopulation!");
            }



            // Clear all of the tables
            //exhDao.clearAllTables();
            //shouldForcePopulate = false;

            var exhibits = Exhibit.fromJson(exhibitsReader);
            exhDao.insert(exhibits);

//            var trails = Trail.fromJson(trailsReader);
//            trailsDao().insert(trails);
        }

        // initialize views
        listView = findViewById(R.id.result_list);
        selectedListView = findViewById(R.id.selected_list);
        selectedCount = findViewById(R.id.count);
        planButton = findViewById(R.id.plan_button);
        searchView = findViewById(R.id.search_bar);

        // make the views disappear
        selectedListView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        // get from db the items to populate our adapter
        animalNameList = exhDao.getExhibitsWithGroups();

        // set and populate adapter
        adapter = new SearchListItemAdapter(this, animalNameList);
        listView.setAdapter(adapter);
        selectedAdapter = new SelectedListAdapter(this, 0, selectedItems);
        selectedListView.setAdapter(selectedAdapter);

        //set the listeners
        searchView.setOnQueryTextListener(new QueryListener(this, exhDao, adapter, listView, selectedListView));
        listView.setOnItemClickListener(this::onItemClicked);
        planButton.setOnClickListener(this::onPlanClicked);
    }


    public void selectEntry(SearchListItem query, int position) {
        this.adapter.remove(this.adapter.getItem(position));
        Log.d("SearchListActivity", "Adding to select: " + query);
        this.selectedItems.add(query);
    }


    public void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
        Exhibit ew = (Exhibit) adapterView.getItemAtPosition(position); //TODO: AdapterView not containing exhibits yet
        Log.d("SearchListActivity", "Adding exhibit to selectedItems:" + ew.toString());
        selectEntry(ew, position);
        searchView.setQuery("", false);
        adapter.notifyDataSetChanged();
        selectedAdapter.notifyDataSetChanged();
        selectedListView.setVisibility(View.VISIBLE);
        selectedCount.setText(String.valueOf(selectedItems.size()));
    }

    public void onPlanClicked(View view) {
        if (!selectedItems.isEmpty()) {
            Intent intent = new Intent(SearchListActivity.this, DirectionActivity.class);
            ArrayList<Exhibit> arraySelectedItems = new ArrayList<>(Exhibit); //TODO: make directionActivity take exhibits
            Log.d("SearchListActivity", "Adding Arraylist of selectedItems to extra:" + arraySelectedItems.toString());
            intent.putParcelableArrayListExtra("selected_list", arraySelectedItems); //TODO: Make parcelable take exhibits
            startActivity(intent);
        } else {
            // Toast that they dum dum
            Toast.makeText(this, "Ooopsie you can't plan nothing silly!", Toast.LENGTH_SHORT).show();
        }
    }


}

