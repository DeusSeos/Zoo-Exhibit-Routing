package com.example.zooseeker;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zooseeker.db.Exhibit;
import com.example.zooseeker.db.Trail;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// might be an apex reference
public class Pathfinder {

    private Context context;
    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, Exhibit> exhibitInfo;
    private Map<String, Trail> trailInfo;
    private List<String> sortedSelectedItemsIDs;
    private List<Exhibit> selectedItems;
    private List<String> tempSelectedItemsIDs = new ArrayList<>();
    private ArrayList<ArrayList<String>> fullPath = new ArrayList<>();
    private DijkstraShortestPath<String, IdentifiedWeightedEdge> dijkstra;
    private int fullPathIndex;


    // Constructor for Pathfinder object
    // Get selectedItems and populate the exhibitInfo and trailInfo
    public Pathfinder(Context context, List<Exhibit> selectedItems) {
        this.context = context;
        g = ZooData.loadZooGraphJSON(context, "sample_zoo_graph.json");
        dijkstra = new DijkstraShortestPath<>(g);

        // Populate from the default assets (note: in your own tests, perhaps use test-only assets?)
        InputStreamReader exhibitsReader;
        InputStreamReader trailsReader;
        try {
            exhibitsReader = new InputStreamReader(context.getAssets().open("sample_node_info.json"));
        } catch (IOException e) {
            Log.d("Pathfinder", e.getLocalizedMessage());
        }
        try {
            trailsReader = new InputStreamReader(context.getAssets().open("sample_edge_info.json"));
        } catch (IOException e) {
            Log.d("Pathfinder", e.getLocalizedMessage());
        }


        this.selectedItems = selectedItems;
        // this is the naive no check approach (change this to at least check if we go over the list index)
        this.fullPathIndex = 0;
        for (Exhibit item : selectedItems) {
            tempSelectedItemsIDs.add(item.id);
        }
    }


    //Create a list of the ids of our selected attractions in an optimized visiting order, also ensures that the list begins and ends with the entrance/exit gate
    //(maybe there's a more optimized route plan we can make I just took shortest distance from the start and kept figuring out which one was the shortest distance from each
    //subsequent stop)
    public void optimizeSelectedItemsIDs() {
        //Ensure that the order begins at the entrance gate
<<<<<<< HEAD

=======
        sortedSelectedItemsIDs = new ArrayList<>();
        String sourceID = "entrance_exit_gate";


        while (!tempSelectedItemsIDs.isEmpty()) {
            int length = tempSelectedItemsIDs.size();
            String lowestID = "";
//            int lowestWeight = Double.MAX_VALUE;
//            GraphPath <>tempPath;
            for (int i = 0; i < length; i++) {
                String sinkID = tempSelectedItemsIDs.get(i);
                DijkstraShortestPath.findPathBetween(g, sourceID, sinkID);

            }



        }
>>>>>>> f2fce438d49d8967fcd61512da2945b097a7489e


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
        String defaultMessage = "  %d. Walk %.0f meters along %s between '%s' and '%s'.\n";
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            directions.add(String.format(Locale.ENGLISH, defaultMessage, i,
                    g.getEdgeWeight(e),
                    trailInfo.get(e.getId()).street,
                    exhibitInfo.get(g.getEdgeSource(e)).name,
                    exhibitInfo.get(g.getEdgeTarget(e)).name));
            i++;
        }
        Log.d("Pathfinder", directions.toString());
        return directions;
    }

    public Double getDistance(GraphPath<String, IdentifiedWeightedEdge> path) {
//        Extract the distance from all the path distances
        Double totalWeight = 0.0;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            totalWeight += g.getEdgeWeight(e);
        }
        return totalWeight;
    }

}
