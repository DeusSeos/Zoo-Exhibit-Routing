package com.example.zooseeker;


import android.content.Context;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// might be an apex reference
public class Pathfinder {

    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private List<String> sortedSelectedItemsIDs;
    private List<SearchListItem> selectedItems;

    public Pathfinder(Context context, List<SearchListItem> selectedItems) {
        g = ZooData.loadZooGraphJSON(context, "sample_zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");
        this.selectedItems = selectedItems;

    }

    public Pathfinder(Context context, String gPath, String vInfoPath, String eInfoPath, List<SearchListItem> selectedItems) {
        g = ZooData.loadZooGraphJSON(context, gPath);
        vInfo = ZooData.loadVertexInfoJSON(context, vInfoPath);
        eInfo = ZooData.loadEdgeInfoJSON(context, eInfoPath);
        this.selectedItems = selectedItems;
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



}
