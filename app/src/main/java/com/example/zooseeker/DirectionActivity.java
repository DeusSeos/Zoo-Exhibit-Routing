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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity {

    String firstLocation = "entrance_exit_gate";
    String secondLocation, thirdLocation = "arctic_foxes";
    private ListView directionList;
    private Button nextButton;
    ArrayList<SearchListItem> selectedItems;
    String zooJsonName;
    String nextLocationName;
    float nextLocationDistance;

    ArrayList<String> directionsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        directionList = findViewById(R.id.direction_list);

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
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");


        //Create array to loop directions into
        ArrayAdapter<String> directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);

        //parse the locations that are selected and run dijkstra between each node (? maybe not)
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, firstLocation, secondLocation);
//        GraphPath<String, IdentifiedWeightedEdge> nextPath = DijkstraShortestPath.findPathBetween(g, secondLocation, thirdLocation);

        //save the directions
//        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
//            String currDir = MessageFormat.format("Walk {0} meters along {1}from {2}to {3}.", g.getEdgeWeight(e), eInfo.get(e.getId()).street, vInfo.get(g.getEdgeSource(e).toString()).name, vInfo.get(g.getEdgeTarget(e).toString()).name);
//            directionsArray.add(currDir);
//        }

        //display the directions in our directionList
//        directionList.setAdapter(directionsAdapter);
//
//        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
//            nextLocationDistance += g.getEdgeWeight(e);
//        }
//
//        String nextButtonText = "Next | " + nextLocationName.toString() + " meters -" + nextLocationName;
//        nextButton.setText(String.valueOf(nextButtonText));

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