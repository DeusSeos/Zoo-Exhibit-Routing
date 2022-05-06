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

    String firstLocation = "entrance_exit_gate";
    String secondLocation = "arctic_foxes";
    String thirdLocation = "arctic_foxes";

    private ListView directionList;
    private Button nextButton;
    ArrayList<SearchListItem> selectedItems;
    ArrayList<String> sortedSelectedItemsIDs;
    Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");
    Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
    Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");
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



        //Create array to loop directions into
        ArrayAdapter<String> directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);

        //parse the locations that are selected and run dijkstra between each node (? maybe not)
//        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, firstLocation, secondLocation);
//        GraphPath<String, IdentifiedWeightedEdge> nextPath = DijkstraShortestPath.findPathBetween(g, secondLocation, thirdLocation);



        //save the directions
//        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
//            String currDir = MessageFormat.format("Walk {0} meters along {1}from {2}to {3}.", g.getEdgeWeight(e), eInfo.get(e.getId()).street, vInfo.get(g.getEdgeSource(e).toString()).name, vInfo.get(g.getEdgeTarget(e).toString()).name);
//            directionsArray.add(currDir);
//        }

        //display the directions in our directionList
        directionList.setAdapter(directionsAdapter);

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


    //Create a list of the ids of our selected attractions in an optimized visiting order, also ensures that the list begins and ends with the entrance/exit gate
    //(maybe there's a more optimized route plan we can make I just took shortest distance from the start and kept figuring out which one was the shortest distance from each
    //subsequent stop)
    public void optimizeSelectedItemsIDs(){
        ArrayList<String> tempSelectedItemsIDs = selectedItems.stream().map(SearchListItem -> SearchListItem.id).collect(Collectors.toCollection(ArrayList::new));

        //Ensure that the order begins at the entrance gate
        sortedSelectedItemsIDs = new ArrayList<String>();
        sortedSelectedItemsIDs.add("entrance_exit_gate");

        //Loop through each ID in selectedItemsIDs, removing it from the tempSelectedItems list as we do (so that
        // we aren't checking an item's distance from itself or already visited attractions) (this list will update each time the loop runs)
        for(String sourceItemID:sortedSelectedItemsIDs){
            tempSelectedItemsIDs.remove(sourceItemID);

            //For each id in tempSelectedItemsIDs, check which is the shortest distance (has the lowest weight) from sourceItemID,
            //and then add it into the sortedSelectedItemsIDs list
            String lowestWeightId = null;
            double lowestWeightVal = Double.MAX_VALUE;
            for(String destItemID:tempSelectedItemsIDs){
                GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, sourceItemID, destItemID);
                if(path.getWeight() < lowestWeightVal){
                    lowestWeightId = destItemID;
                    lowestWeightVal = path.getWeight();
                }
            }
            sortedSelectedItemsIDs.add(lowestWeightId);

        }
        //Ensure it ends at the entrance/exit gate
        sortedSelectedItemsIDs.add("entrance_exit_gate");

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