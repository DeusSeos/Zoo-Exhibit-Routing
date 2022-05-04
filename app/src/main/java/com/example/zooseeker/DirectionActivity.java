package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity {

    String start = "entrance_exit_gate";
    String goal = "arctic_foxes";
    private ListView directionsList;
    private Button nextButton;
    String zooJsonName;
    ArrayList<String> directionsArray;
    LinkedHashSet<SearchListItem> selectedItems = (LinkedHashSet<SearchListItem>) getIntent().getSerializableExtra("selected_list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        directionsList = findViewById(R.id.direction_list);
        nextButton = findViewById(R.id.next_button);

        //generate the graph - Currently uses example asset
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");

        //create a new list view
        directionsList = (ListView) findViewById(R.id.direction_list);

        //Create array to loop directions into
        ArrayAdapter<String> directionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directionsArray);

        //parse the locations that are selected and run dijkstra between each node (? maybe not)
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);

        //save the directions
        int i = 0;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            String currDir = ("Walk " + g.getEdgeWeight(e) + " meters along " + eInfo.get(e.getId()).street +
                    "from " + vInfo.get(g.getEdgeSource(e).toString()).name + "to " +
                    vInfo.get(g.getEdgeTarget(e).toString()).name + ".");
            directionsArray.add(currDir);
            i++;
        }

        //display the directions in our directionList
        directionsList.setAdapter(directionsAdapter);

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