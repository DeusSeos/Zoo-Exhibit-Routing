package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchListActivity extends AppCompatActivity {


    //Declared variables
    ListView listView;
    SearchView searchView;
    ArrayAdapter<String> adapter;
    List<SearchListItem> animalNameList;
    Set<String> selectedItems = new HashSet<>();
    private SearchDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Log.d("SearchListActivity", "ASL");

//        // Get the intent, verify the action and get the query
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            Log.d("SearchListActivity", "query");
//        }

        db = SearchDatabase.getSingleton(this);


        SearchListDao searchListDao = db.searchListDao();

//        List<SearchListItem> searchListItems = SearchListItem.loadJson(this, "sample_node_info.json");
//        searchListDao.insertAll(searchListItems);

//
        listView = findViewById(R.id.trashy_list);

        List<String> animalNameList = searchListDao.getByName();
        Log.d("SearchListActivity" , "Database names: " + animalNameList.toString());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animalNameList);
        listView.setAdapter(adapter);
//
        searchView = findViewById(R.id.trash);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchListActivity", query);

                ArrayList<String> nameList = (ArrayList<String>) searchListDao.getByInput(query);
                if (animalNameList.containsAll(nameList)) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(SearchListActivity.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("SearchListActivity", query);
                adapter.getFilter().filter(query);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String ew = (String) adapterView.getItemAtPosition(position);
//                String thing = Integer.toString(position);

                Log.d("SearchListActivity", ew);
                selectEntry(ew, position);
                searchView.setQuery("", false);


            }
        });
    }

    public void selectEntry(String query, int position) {
        this.adapter.remove(this.adapter.getItem(position));
        Log.d("SearchListActivity", "Adding to select: " + query);
        this.selectedItems.add(query);
        Log.d("SearchListActivity", "Selected Items updated: " + this.selectedItems.toString());
    }
}