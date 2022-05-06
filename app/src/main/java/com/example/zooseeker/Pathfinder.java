package com.example.zooseeker;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

// might be an apex reference
public class Pathfinder {

    private Context context;
    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private List<String> sortedSelectedItemsIDs;
    private List<SearchListItem> selectedItems;
    private List<String> tempSelectedItemsIDs = new ArrayList<>();
    private ArrayList<ArrayList<String>> fullPath = new ArrayList<>();
    private int fullPathIndex;

    public Pathfinder(Context context, List<SearchListItem> selectedItems) {
        this.context = context;
        g = ZooData.loadZooGraphJSON(context, "sample_zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");
        this.selectedItems = selectedItems;
        // this is the naive no check approach (change this to at least check if we go over the list index)
        this.fullPathIndex = 0;
        for (SearchListItem item: selectedItems) {
            tempSelectedItemsIDs.add(item.id);
        }
    }

    public Pathfinder(Context context, String gPath, String vInfoPath, String eInfoPath, List<SearchListItem> selectedItems) {
        this.context = context;
        g = ZooData.loadZooGraphJSON(context, gPath);
        vInfo = ZooData.loadVertexInfoJSON(context, vInfoPath);
        eInfo = ZooData.loadEdgeInfoJSON(context, eInfoPath);
        this.selectedItems = selectedItems;
        // this is the naive no check approach (change this to at least check if we go over the list index)
        this.fullPathIndex = 0;
        for (SearchListItem item: selectedItems) {
            tempSelectedItemsIDs.add(item.id);
        }

    }

    //Create a list of the ids of our selected attractions in an optimized visiting order, also ensures that the list begins and ends with the entrance/exit gate
    //(maybe there's a more optimized route plan we can make I just took shortest distance from the start and kept figuring out which one was the shortest distance from each
    //subsequent stop)
    public void optimizeSelectedItemsIDs() {
        //Ensure that the order begins at the entrance gate
        sortedSelectedItemsIDs = new ArrayList<>();
        sortedSelectedItemsIDs.add("entrance_exit_gate");
        String sourceItemID = "entrance_exit_gate";
        GraphPath<String, IdentifiedWeightedEdge> path;
        String lastItem = "entrance_exit_gate";
        if (!tempSelectedItemsIDs.isEmpty()) {
            //Loop through each ID in selectedItemsIDs, removing it from the tempSelectedItems list as we do (so that
            // we aren't checking an item's distance from itself or already visited attractions) (this list will update each time the loop runs)
            while (!tempSelectedItemsIDs.isEmpty()) {
                //For each id in tempSelectedItemsIDs, check which is the shortest distance (has the lowest weight) from sourceItemID,
                //and then add it into the sortedSelectedItemsIDs list
                String lowestWeightId = null;
                double lowestWeightVal = Double.MAX_VALUE;
                int i = 0;
                for (String destItemID : tempSelectedItemsIDs) {
                    path = DijkstraShortestPath.findPathBetween(g, sourceItemID, destItemID);
                    if (path != null) {
                        if (path.getWeight() < lowestWeightVal) {
                            lowestWeightId = destItemID;
                            lowestWeightVal = path.getWeight();
                        }
                        sortedSelectedItemsIDs.add(lowestWeightId);
                        tempSelectedItemsIDs.remove(lowestWeightId);
                        sourceItemID = lowestWeightId;
                        lastItem = lowestWeightId;
                        // initialize this cause some stupid af concurrent issue (java.util.ConcurrentModificationException) idk i hate it here
                        ArrayList<String> dir = getDirections(path);
                        fullPath.add(dir);
                        ++i;
                    }
                    // else we skip
                }
            }
            //Ensure it ends at the entrance/exit gate
            sortedSelectedItemsIDs.add("entrance_exit_gate");
            // run dijkstras again from last item to entrance
            path = DijkstraShortestPath.findPathBetween(g, lastItem, "entrance_exit_gate");
            fullPath.add(getDirections(path));
            Log.d("Pathfinder", sortedSelectedItemsIDs.toString());
        } else {
//            it was empty for some reason so gotta go back to beginning
            sortedSelectedItemsIDs.add("entrance_exit_gate");
            // run dijkstras again from last item to entrance
            path = DijkstraShortestPath.findPathBetween(g, sourceItemID, lastItem);
            fullPath.add(getDirections(path));

        }
        Log.d("Pathfinder" , fullPath.toString());

    }

    public ArrayList<String> next() {
        if (fullPathIndex < fullPath.size()) {
            // return the next path from fullpath
            return fullPath.get(fullPathIndex++);
        } else {
            Toast.makeText(context, "This is the end!", Toast.LENGTH_LONG).show();
            ArrayList<String> noMore = new ArrayList<>();
            noMore.add("No more");
            return noMore;
        }
    }

    public ArrayList<String> getDirections(GraphPath<String, IdentifiedWeightedEdge> path) {
        ArrayList<String> directions = new ArrayList<>();
        int i = 1;
        String defaultMessage = "  %d. Walk %.0f meters along %s from '%s' to '%s'.\n";
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            directions.add(String.format(Locale.ENGLISH, defaultMessage, i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e)).name,
                    vInfo.get(g.getEdgeTarget(e)).name));
            i++;
        }
        Log.d("Pathfinder", directions.toString());
        return directions;
    }

}
