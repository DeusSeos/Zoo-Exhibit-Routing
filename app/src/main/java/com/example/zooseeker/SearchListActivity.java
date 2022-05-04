package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchListActivity extends AppCompatActivity {


    //Declared variables
    ListView listView;
    SearchView searchView;
    SearchListItemAdapter adapter;
    List<SearchListItem> animalNameList;
    Set<SearchListItem> selectedItems = new HashSet<>();
    private SearchDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        db = SearchDatabase.getSingleton(this);
        SearchListDao searchListDao = db.searchListDao();


       searchListDao.deleteAll();
       List<SearchListItem> searchListItems = SearchListItem.loadJson(this, "sample_node_info.json");
       searchListDao.insertAll(searchListItems);

        listView = findViewById(R.id.result_list);
        listView.setVisibility(View.GONE);
        animalNameList = searchListDao.getAll();

        adapter = new SearchListItemAdapter(this, animalNameList);
        listView.setAdapter(adapter);



        listView.setEmptyView(findViewById(R.id.empty));
//
        searchView = findViewById(R.id.search_bar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchListActivity", query);
                List<SearchListItem> nameList =  searchListDao.getItemsByInput(query);
                if (animalNameList.containsAll(nameList)) {
                    adapter.getFilter().filter(query, new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int i) {
                            listView.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(SearchListActivity.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("SearchListActivity", query);
                if (TextUtils.isEmpty(query)){
                    listView.setVisibility(View.GONE);

                } else {
                    adapter.getFilter().filter(query, new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int i) {
                            listView.setVisibility(View.VISIBLE);
                        }
                    });
                }


                return false;
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SearchListItem ew = (SearchListItem) adapterView.getItemAtPosition(position);
                String thing = Integer.toString(position);
                Log.d("SearchListActivity", ew.toString());
                selectEntry(ew, position);
                searchView.setQuery("", false);
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void selectEntry(SearchListItem query, int position) {
        this.adapter.remove(this.adapter.getItem(position));
        Log.d("SearchListActivity", "Adding to select: " + query);
        this.selectedItems.add(query);
        Log.d("SearchListActivity", "Selected Items updated: " + this.selectedItems.toString());
    }
}