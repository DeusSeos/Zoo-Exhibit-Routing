package com.example.zooseeker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zooseeker.adapters.ExhibitAdapter;
import com.example.zooseeker.adapters.SelectedExhibitAdapter;
import com.example.zooseeker.db.ExhibitWithGroup;
import com.example.zooseeker.db.ExhibitsDao;
import com.example.zooseeker.db.IDDao;
import com.example.zooseeker.db.PersistenceDatabase;
import com.example.zooseeker.db.SearchDatabase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class SearchListActivity extends AppCompatActivity {

    //Declared variables
    Button planButton;
    TextView selectedCount;
    ListView listView;
    ListView selectedListView;
    SearchView searchView;
    ExhibitAdapter adapter;
    SelectedExhibitAdapter selectedAdapter;

    ExhibitsDao exhibitsDao;

    IDDao idDao;
    ArrayList<ExhibitWithGroup> animalNameList = new ArrayList<>();
    LinkedHashSet<ExhibitWithGroup> selectedItems = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_list);

        // initialize db stuff
        SearchDatabase db = SearchDatabase.getDatabase(this);
        PersistenceDatabase persistenceDatabase = PersistenceDatabase.getSingleton(this);

        idDao = persistenceDatabase.IDDao();
        exhibitsDao = db.exhibitsDao();
//        ZooDatabase db = ZooDatabase.getDatabase(this);


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


        Log.d("SearchListActivity", "Exhibits: " + exhibitsDao.getExhibits().toString());
        Log.d("SearchListActivity", "Exhibits: " + exhibitsDao.getExhibits().toString());
        animalNameList = new ArrayList<>(exhibitsDao.getExhibits());
        Log.d("SearchListActivity", "animalNameList: " + animalNameList.toString());

        // set and populate adapter
        adapter = new ExhibitAdapter(this, animalNameList);
        listView.setAdapter(adapter);
        selectedAdapter = new SelectedExhibitAdapter(this, 0, selectedItems);
        selectedListView.setAdapter(selectedAdapter);

        //set the listeners
        searchView.setOnQueryTextListener(new QueryListener(this, exhibitsDao, adapter, listView, selectedListView));
        listView.setOnItemClickListener(this::onItemClicked);
        planButton.setOnClickListener(this::onPlanClicked);


    }


    @Override
    protected void onStop() {
        Log.i("SearchListActivity", "Stopping...");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("SearchListActivity", "Restarting...");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("SearchListActivity", "Destroying...");
        List<String> selectedIDs = selectedItems.stream().map(ExhibitWithGroup::getExhibitID).collect(Collectors.toList());
        idDao.insert(selectedIDs);
        super.onDestroy();
    }

    public void selectEntry(ExhibitWithGroup query, int position) {
        this.adapter.remove(this.adapter.getItem(position));
        Log.d("SearchListActivity", "Adding to select: " + query);
        this.selectedItems.add(query);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("exhibits", animalNameList);
    }

    public void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
        ExhibitWithGroup ew =  (ExhibitWithGroup) adapterView.getItemAtPosition(position); //TODO: AdapterView not containing exhibits yet
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
            ArrayList<ExhibitWithGroup> arraySelectedItems = new ArrayList<>(selectedItems); //TODO: make directionActivity take exhibits
            Log.d("SearchListActivity", "Adding Arraylist of selectedItems to extra:" + arraySelectedItems);
            intent.putParcelableArrayListExtra("selected_list", arraySelectedItems); //TODO: Make parcelable take exhibits
            startActivity(intent);
        } else {
            // Toast that they dum dum
            Toast.makeText(this, "Ooopsie you can't plan nothing silly!", Toast.LENGTH_SHORT).show();
        }
    }


}