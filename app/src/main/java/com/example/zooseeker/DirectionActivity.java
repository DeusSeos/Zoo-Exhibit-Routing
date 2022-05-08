package com.example.zooseeker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.GraphWalk;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DirectionActivity extends AppCompatActivity {

    private Pathfinder pathy;
    private ListView directionList;
    private Button nextButton;
    private ArrayList<SearchListItem> selectedItems;
    private String zooJsonName;
    private String nextLocationName;
    private float nextLocationDistance;

    List<String> directionsArray = new ArrayList<>();
    private ArrayAdapter<String> directionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        // Initialize variables
        directionList = findViewById(R.id.direction_list);
        nextButton = findViewById(R.id.next_button);

        // Try to load the selected items list from previous activity
        if (getIntent().getParcelableArrayListExtra("selected_list") != null){
            selectedItems = getIntent().getParcelableArrayListExtra("selected_list");
            Log.d("DirectionActivity", selectedItems.toString());
        } else {
            Log.d("DirectionActivity", "trash :)");
        }

        pathy = new Pathfinder(this, selectedItems);
        // could make this a call in the constructor (depends if we want to always optimize path first or not)
        pathy.optimizeSelectedItemsIDs();

        directionsArray = pathy.next();

        //Create array to loop directions into
        directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
        directionList.setAdapter(directionsAdapter);

//        String nextButtonText = "Next | " + nextLocationName.toString() + " meters -" + nextLocationName;
//        nextButton.setText(nextButtonText);

        //move through to the next item in the list


        //edit nextButton onClick
        nextButton.setOnClickListener(view -> {
            directionsArray = pathy.next();
            Log.d("DirectionActivity", directionsArray.toString());
            directionsAdapter  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
            // update to Next (next attraction, distance to it)
        });





    }



}