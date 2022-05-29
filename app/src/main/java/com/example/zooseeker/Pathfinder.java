package com.example.zooseeker;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zooseeker.db.Exhibit;
import com.example.zooseeker.db.ExhibitWithGroup;
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

import javax.xml.transform.Source;

// might be an apex reference
public class Pathfinder {

    private Context context;
    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ExhibitWithGroup> vInfo;
    private Map<String, Trail> eInfo;
    private List<String> sortedSelectedItemsIDs;
    private List<ExhibitWithGroup> selectedItems;
    private List<String> tempSelectedItemsIDs = new ArrayList<>();
    private ArrayList<ArrayList<String>> fullPath = new ArrayList<>();
    private DijkstraShortestPath<String, IdentifiedWeightedEdge> dijkstra;
    private int fullPathIndex;


    // Constructor for Pathfinder object
    // Get selectedItems and populate the exhibitInfo and trailInfo
    public Pathfinder(Context context, List<ExhibitWithGroup> selectedItems) {
        this.context = context;
        g = ZooData.loadZooGraphJSON(context, "zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context, "exhibit_info");
        eInfo = ZooData.loadEdgeInfoJSON(context, "trail_info");

        dijkstra = new DijkstraShortestPath<>(g);

        this.selectedItems = selectedItems;
        // this is the naive no check approach (change this to at least check if we go over the list index)
        this.fullPathIndex = 0;
        for (ExhibitWithGroup item : selectedItems) {
            if (item.exhibit.hasGroup()){
                tempSelectedItemsIDs.add(item.group.id);
            } else {
                tempSelectedItemsIDs.add(item.exhibit.id);
            }

        }
        Log.d("Pathfinder", "temp Selected Items" + tempSelectedItemsIDs.toString());
    }


    //Create a list of the ids of our selected attractions in an optimized visiting order, also ensures that the list begins and ends with the entrance/exit gate
    //(maybe there's a more optimized route plan we can make I just took shortest distance from the start and kept figuring out which one was the shortest distance from each
    //subsequent stop)
    public void optimizeSelectedItemsIDs() {
        //Ensure that the order begins at the entrance gate
<<<<<<< HEAD
        sortedSelectedItemsIDs = new ArrayList<>();
        String sourceID = "entrance_exit_gate";
=======

>>>>>>> 99de6f0fed7f0d2b7120931da9ce6728fc942e5a

        sortedSelectedItemsIDs = new ArrayList<>();
        sortedSelectedItemsIDs.add("entrance_exit_gate");

        String sourceID = "entrance_exit_gate";
        String tempSource = "temp";
        double shortest = Double.MAX_VALUE;
        Log.d("Pathfinder", "sourceId:" + sourceID);
        Log.d("Pathfinder", g.vertexSet().toString());
        Log.d("Pathfinder", tempSelectedItemsIDs.toString());
        // Get a sorted strings of exihibits
        while (!tempSelectedItemsIDs.isEmpty()) {
            for (String sink : tempSelectedItemsIDs) {
                GraphPath<String, IdentifiedWeightedEdge> path =  DijkstraShortestPath.findPathBetween(g ,sourceID, sink);
                double curr = this.getDistance(path);
                Log.d("Pathfinder", "sourceId:" + sourceID + " sink:" + sink);
                if (curr < shortest) {
                    shortest = curr;
                    tempSource = sink;
                }

                sourceID = tempSource;
                sortedSelectedItemsIDs.add(sourceID);
                tempSelectedItemsIDs.remove(sourceID);
            }
        }

        sortedSelectedItemsIDs.add("entrance_exit_gate");
        int startIndex = 0;
        int goalIndex = 1;
        String sourceItemID;

        while (goalIndex < sortedSelectedItemsIDs.size()) {
            sourceItemID = sortedSelectedItemsIDs.get(startIndex++);
            String goalID = sortedSelectedItemsIDs.get(goalIndex++);
            Log.d("Pathfinder", sourceItemID);
            Log.d("Pathfinder", goalID);
            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, sourceItemID, goalID);
            Log.d("Pathfinder", "HIIIII: " + getDirections(path));
            fullPath.add(getDirections(path));

        }
<<<<<<< HEAD

=======
        Log.d("Pathfinder", "FULL PATH LOSER: " + fullPath.toString());
>>>>>>> 99de6f0fed7f0d2b7120931da9ce6728fc942e5a

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
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e)).exhibit.name,
                    vInfo.get(g.getEdgeTarget(e)).exhibit.name));
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
