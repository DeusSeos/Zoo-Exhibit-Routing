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

    List<SearchListItem> animalNameList;
    LinkedHashSet<SearchListItem> selectedItems = new LinkedHashSet<>();

    private SearchDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        db = SearchDatabase.getSingleton(this);
        SearchListDao searchListDao = db.searchListDao();


//       searchListDao.deleteAll();

        if (searchListDao.getAll() == null) {
            List<SearchListItem> searchListItems = SearchListItem.loadJson(this, "sample_node_info.json");
            searchListDao.insertAll(searchListItems);
        }


        listView = findViewById(R.id.result_list);
        selectedListView = findViewById(R.id.selected_list);
        selectedCount = findViewById(R.id.count);
        planButton = findViewById(R.id.plan_button);

        selectedListView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        animalNameList = searchListDao.getAll();

        adapter = new SearchListItemAdapter(this, animalNameList);
        listView.setAdapter(adapter);
        selectedAdapter = new SelectedListAdapter(this, 0, selectedItems);
        selectedListView.setAdapter(selectedAdapter);

//        listView.setEmptyView(findViewById(R.id.empty));

        searchView = findViewById(R.id.search_bar);

//        searchView.setOnQueryTextListener(this::onTextChange);

        searchView.setOnQueryTextListener(new QueryListener(this, searchListDao, adapter, listView, selectedListView));


        listView.setOnItemClickListener(this::onItemClicked);
        planButton.setOnClickListener(this::onPlanClicked);
    }

    public void selectEntry(SearchListItem query, int position) {
        this.adapter.remove(this.adapter.getItem(position));
        Log.d("SearchListActivity", "Adding to select: " + query);
        this.selectedItems.add(query);
        //selectedAdapter.add(query);
        Log.d("SearchListActivity", "Selected Items updated: " + this.selectedItems.toString());
        Log.d("SearchListActivity", "Adapter updated: " + selectedAdapter.toString());
    }


    public void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
        SearchListItem ew = (SearchListItem) adapterView.getItemAtPosition(position);
        Log.d("SearchListActivity", ew.toString());
        selectEntry(ew, position);
        searchView.setQuery("", false);
        adapter.notifyDataSetChanged();
        selectedAdapter.notifyDataSetChanged();
        selectedListView.setVisibility(View.VISIBLE);
        selectedCount.setText(String.valueOf(selectedItems.size()));
    }

    public void onPlanClicked(View view) {
        Intent intent = new Intent(SearchListActivity.this, DirectionActivity.class);
        ArrayList<SearchListItem> arraySelectedItems = new ArrayList<>(selectedItems);
        Log.d("SearchListActivity", arraySelectedItems.toString());
        intent.putParcelableArrayListExtra("selected_list", arraySelectedItems);
        startActivity(intent);
    }


}

