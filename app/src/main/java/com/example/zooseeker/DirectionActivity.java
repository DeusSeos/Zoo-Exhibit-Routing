package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity {

    private ListView directionList;
    private Button nextButton;
    ArrayList<SearchListItem> selectedItems;
    String zooJsonName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        if (getIntent().getParcelableArrayListExtra("selected_list") != null){
            selectedItems = getIntent().getParcelableArrayListExtra("selected_list");
            Log.d("DirectionActivity", selectedItems.toString());
        } else {
            Log.d("DirectionActivity", "trash :)");
        }

        directionList = findViewById(R.id.direction_list);
        nextButton = findViewById(R.id.next_button);

        //generate the graph - Currently uses example asset
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");

        //create a new list view
        //parse the locations that are selected and run dijkstra between each node
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);

        //save the directions

        //display the directions in our directionList

        //move through to the next item in the list


        //edit nextButton onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update to Next (next attraction, distance to it)
            }
        });





    }
}