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
import java.util.Map;
import java.util.stream.Collectors;

public class DirectionActivity extends AppCompatActivity {

    private ListView directionList;
    private Button nextButton;
    private ArrayList<SearchListItem> selectedItems;
    String zooJsonName;
    String nextLocationName;
    float nextLocationDistance;

    ArrayList<String> directionsArray = new ArrayList<>();

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

        //Create array to loop directions into
        ArrayAdapter<String> directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
        directionList.setAdapter(directionsAdapter);


        //display the directions in our directionList


//        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
//            nextLocationDistance += g.getEdgeWeight(e);
//        }

//        String nextButtonText = "Next | " + nextLocationName.toString() + " meters -" + nextLocationName;
//        nextButton.setText(nextButtonText);

        //move through to the next item in the list


        //edit nextButton onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update to Next (next attraction, distance to it)
            }
        });





    }




    /*
    public GraphPath<String, IdentifiedWeightedEdge>  generatePath(String start, String goal){
        GraphPath<String, IdentifiedWeightedEdge> path = new
        //if start or goal are not vertices
        // return an empty path
        if (!vInfo.containsKey(start) || !vInfo.containsKey(goal)){

        } else {
            path = DijkstraShortestPath.findPathBetween(g, start, goal);
        }
        // try to generate path
        // catch error and display in a toast notifaction
        return path;
    }
     */



}