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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// might be an apex reference
public class Pathfinder {

    private Context context;
    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, Exhibit> vInfo;
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
        Log.d("info", "vInfo: " + vInfo.entrySet());
        Log.d("info", "eInfo: " + eInfo.entrySet());

    }


    //Create a list of the ids of our selected attractions in an optimized visiting order, also ensures that the list begins and ends with the entrance/exit gate
    //(maybe there's a more optimized route plan we can make I just took shortest distance from the start and kept figuring out which one was the shortest distance from each
    //subsequent stop)
    public void optimizeSelectedItemsIDs() {
        //Ensure that the order begins at the entrance gate
        sortedSelectedItemsIDs = new ArrayList<>();
        //String sourceID = "entrance_exit_gate";

        sortedSelectedItemsIDs = new ArrayList<>();
        sortedSelectedItemsIDs.add("entrance_exit_gate");

        String sourceID = "entrance_exit_gate";
        String tempSource = "temp";
        double shortest = Double.MAX_VALUE;
        Log.d("Pathfinder", "sourceId:" + sourceID);
        Log.d("Pathfinder", g.vertexSet().toString());
        Log.d("Pathfinder", tempSelectedItemsIDs.toString());
        // Get a sorted strings of exhibits

        while (!tempSelectedItemsIDs.isEmpty()) {
            for (String sink : tempSelectedItemsIDs) {
                GraphPath<String, IdentifiedWeightedEdge> path =  DijkstraShortestPath.findPathBetween(g ,sourceID, sink);
                double curr = this.getDistance(path);
                Log.d("Pathfinder", "sourceId:" + sourceID + " sink:" + sink);
                if (curr < shortest) {
                    shortest = curr;
                    tempSource = sink;
                }
            }
            shortest = Double.MAX_VALUE;
            sourceID = tempSource;
            sortedSelectedItemsIDs.add(sourceID);
            tempSelectedItemsIDs.remove(sourceID);
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
            for (IdentifiedWeightedEdge e: path.getEdgeList()){
                Log.d("Pathfinder", "E: " + e.toString());
            }

//            Log.d("Pathfinder", "HIIIII: " + getDirections(path));
            fullPath.add(getDirections(path));

        }
        Log.d("Pathfinder", "FULL PATH LOSER: " + fullPath.toString());

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

    public ArrayList<String> back() {
        if (fullPathIndex >= 0) {
            // return the next path from fullpath
            return fullPath.get(fullPathIndex--);
        } else {
            Toast.makeText(context, "Can't go back any further", Toast.LENGTH_SHORT).show();
            return fullPath.get(0);
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
                    vInfo.get(g.getEdgeSource(e)).name,
                    vInfo.get(g.getEdgeTarget(e)).name));
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

    public void optimizeBriefSelectedItemsIDs() {
        //Ensure that the order begins at the entrance gate
        sortedSelectedItemsIDs = new ArrayList<>();
        //String sourceID = "entrance_exit_gate";

        sortedSelectedItemsIDs = new ArrayList<>();
        sortedSelectedItemsIDs.add("entrance_exit_gate");

        String sourceID = "entrance_exit_gate";
        String tempSource = "temp";
        double shortest = Double.MAX_VALUE;
        Log.d("Pathfinder", "sourceId:" + sourceID);
        Log.d("Pathfinder", g.vertexSet().toString());
        Log.d("Pathfinder", tempSelectedItemsIDs.toString());
        // Get a sorted strings of exhibits

        while (!tempSelectedItemsIDs.isEmpty()) {
            for (String sink : tempSelectedItemsIDs) {
                GraphPath<String, IdentifiedWeightedEdge> path =  DijkstraShortestPath.findPathBetween(g ,sourceID, sink);
                double curr = this.getDistance(path);
                //Log.d("Pathfinder", "sourceId:" + sourceID + " sink:" + sink);
                if (curr < shortest) {
                    shortest = curr;
                    tempSource = sink;
                }
            }
            shortest = Double.MAX_VALUE;
            sourceID = tempSource;
            sortedSelectedItemsIDs.add(sourceID);
            tempSelectedItemsIDs.remove(sourceID);
        }

        sortedSelectedItemsIDs.add("entrance_exit_gate");
        int startIndex = 0;
        int goalIndex = 1;
        String sourceItemID;

        while (goalIndex < sortedSelectedItemsIDs.size()) {
            sourceItemID = sortedSelectedItemsIDs.get(startIndex++);
            String goalID = sortedSelectedItemsIDs.get(goalIndex++);
            Log.d("PathfinderBrief", sourceItemID);
            Log.d("PathfinderBrief", goalID);
            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, sourceItemID, goalID);
            for (IdentifiedWeightedEdge e: path.getEdgeList()){
                Log.d("PathfinderBrief", "E: " + e.toString());
            }

//            Log.d("Pathfinder", "HIIIII: " + getDirections(path));
            fullPath.add(getBriefDirections(path));

        }
        Log.d("PathfinderBrief", "FULL PATH LOSER: " + fullPath.toString());

    }
    public ArrayList<String> getBriefDirections(GraphPath<String, IdentifiedWeightedEdge> path) {
        ArrayList<String> directions = new ArrayList<>();
        int i = 1;
        double edgeWeight = 0;
        String j = null;
        String defaultMessage = "  %d. Walk %.0f meters along %s to '%s'.\n";
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            if(j == null) {
                j = e.getId();
                edgeWeight = g.getEdgeWeight(e);
                continue;
            }
            if(eInfo.get(e.getId()).street.equals(eInfo.get(j).street)) {
                Log.d("PathfinderBriefDirection", eInfo.get(e.getId()).street);
                edgeWeight += g.getEdgeWeight(e);
                j = e.getId();
            } else {
                directions.add(String.format(Locale.ENGLISH, defaultMessage, i,
                        edgeWeight,
                        eInfo.get(j).street,
                        eInfo.get(e.getId()).street));
                i++;
                Log.d("PAthFInderstreet", eInfo.get(e.getId()).street);
                j = e.getId();
                edgeWeight = 0 + g.getEdgeWeight(e);
            }
        }
        Log.d("PathfinderBriefDirection", directions.toString());
        return directions;
    }
    public void pathUpdate(List<ExhibitWithGroup> selectedItems, boolean direction) {
        Log.d("pathupdate", "updating");
        this.selectedItems = selectedItems;
        // this is the naive no check approach (change this to at least check if we go over the list index)
        this.fullPathIndex = 0;
        fullPath.clear();
        for (ExhibitWithGroup item : selectedItems) {
            if (item.exhibit.hasGroup()){
                tempSelectedItemsIDs.add(item.group.id);
            } else {
                tempSelectedItemsIDs.add(item.exhibit.id);
            }

        }
        if(direction) {
            optimizeSelectedItemsIDs();
        } else {
            optimizeBriefSelectedItemsIDs();
        }
    }
}
