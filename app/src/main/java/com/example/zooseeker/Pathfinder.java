package com.example.zooseeker;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.zooseeker.db.Exhibit;
import com.example.zooseeker.db.ExhibitWithGroup;
import com.example.zooseeker.db.Trail;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

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
    private HashMap<Integer, List<String>> hash = new HashMap<>();
    public static boolean flag; // serves as global variable to check the dialog
    public Coord targetCoord;
    private int targetSortedIndex;



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
        this.fullPathIndex = -1;

        for (ExhibitWithGroup item : selectedItems) {
            if (item.exhibit.hasGroup()) {
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
    public void optimizeSelectedItemsIDs(String sourceID) {
        //Ensure that the order begins at the entrance gate
        sortedSelectedItemsIDs = new ArrayList<>();
        //String sourceID = "entrance_exit_gate";

        sortedSelectedItemsIDs = new ArrayList<>();
        sortedSelectedItemsIDs.add("entrance_exit_gate");


        Log.d("Pathfinder", "sourceId:" + sourceID);
        Log.d("Pathfinder", g.vertexSet().toString());
        Log.d("Pathfinder", tempSelectedItemsIDs.toString());
        // Get a sorted strings of exhibits

        for(String i : sortID(this.tempSelectedItemsIDs, null)) {
            sortedSelectedItemsIDs.add(i);
        }
        sortedSelectedItemsIDs.add("entrance_exit_gate");
        int startIndex = 0;
        int goalIndex = 1;
        while(goalIndex < sortedSelectedItemsIDs.size()) {
            GraphPath<String, IdentifiedWeightedEdge> path = buildPath(sortedSelectedItemsIDs, startIndex, goalIndex);
//            Log.d("Pathfinder", "HIIIII: " + getDirections(path));
            fullPath.add(getDirections(path, goalIndex));
            startIndex++;
            goalIndex++;
        }

        Log.d("Pathfinder", "Path of length: " + fullPath.size());

    }

    public int mock(String s){
        Double newLat = Double.valueOf(s.split(",")[0]);
        Double newLng = Double.valueOf(s.split(",")[1]);
        Coord newCoord = new Coord(newLat, newLng);

        Log.d("hash", String.valueOf(this.hash.size()));
        Log.d("coords", String.valueOf(targetCoord.lat));
        if (newCoord.hashCode() == this.targetCoord.hashCode()) {

            return -2;
        } else {
            for (int i = 0; i < this.hash.get(this.targetCoord.hashCode()).size(); i++){
                String tmp = this.hash.get(this.targetCoord.hashCode()).get(i);
                Coord stop = new Coord(vInfo.get(tmp).lat, vInfo.get(tmp).lng);
                if (newCoord.equals(stop)){
                    return i;
                }
            }
        }

        return -1;
    }

    public ArrayList<String> update(int flag){
        ArrayList<String> res = new ArrayList<>();

        if (flag == -2){
            res.add("You arrived!");
            return res;
        }

        if (flag == -1){

            return res;
        }

        for (int i = flag; i < this.fullPath.get(fullPathIndex).size(); i++){
            res.add(fullPath.get(fullPathIndex).get(i));
        }


        return res;
    }

    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Off-track!")
                .setMessage(message)
                .setNegativeButton("Don't bother", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Pathfinder.flag = false;
                    }
                })
                .setPositiveButton("Replan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Pathfinder.flag = true;
                    }
                })
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public ArrayList<String> summary() {
        ArrayList<String> res = new ArrayList<>();
        int start = 0;
        int goal = 1;
        res.add("Plan Summary:");
        while (goal < sortedSelectedItemsIDs.size()) {
            String distance = String.valueOf(dijkstra.getPathWeight(sortedSelectedItemsIDs.get(start), sortedSelectedItemsIDs.get(goal)));
            res.add(vInfo.get(sortedSelectedItemsIDs.get(goal)).name + ", " + distance + "m");
            start++;
            goal++;
        }
        return res;
    }


    public ArrayList<String> next() {
        if (fullPathIndex < fullPath.size()-1) {
            // return the next path from fullpath
            fullPathIndex += 1;

            this.targetCoord = Coord.of(vInfo.get(sortedSelectedItemsIDs.get(fullPathIndex+1)).lat,
                    vInfo.get(sortedSelectedItemsIDs.get(fullPathIndex+1)).lng);
            this.targetSortedIndex = this.fullPathIndex+1;


            Log.d("Pathfinder", "Index: " + fullPathIndex);
            return fullPath.get(fullPathIndex);
        } else if (fullPathIndex == fullPath.size()-1) {
            fullPathIndex++;
            Toast.makeText(context, "This is the end!", Toast.LENGTH_LONG).show();
            ArrayList<String> noMore = new ArrayList<>();
            noMore.add("No more");
            return noMore;
        } else {
            Toast.makeText(context, "This is the end!", Toast.LENGTH_LONG).show();
            ArrayList<String> noMore = new ArrayList<>();
            noMore.add("No more");
            return noMore;
        }
    }

    public ArrayList<String> back() {
        if(fullPathIndex == -1) {
            Toast.makeText(context, "You haven't started", Toast.LENGTH_LONG).show();
            return this.summary();
        }
        if (fullPathIndex > 0) {
            this.targetCoord = Coord.of(vInfo.get(sortedSelectedItemsIDs.get(fullPathIndex-1)).lat,
                    vInfo.get(sortedSelectedItemsIDs.get(fullPathIndex-1)).lng);

            // return the next path from fullpath
            Log.d("Pathfinder", "Index: " + fullPathIndex);
            ArrayList<String> currentPath = (ArrayList<String>) fullPath.get(--fullPathIndex).clone();
            Collections.reverse(currentPath);
            fullPathIndex --;
            return currentPath;
        } else {
            Toast.makeText(context, "Can't go back any further", Toast.LENGTH_SHORT).show();
            ArrayList<String> currentPath = (ArrayList<String>) fullPath.get(0).clone();
            Collections.reverse(currentPath);
            return currentPath;
        }
    }

    public ArrayList<String> skip(){
        if(fullPathIndex == -1) {
            Toast.makeText(context, "You haven't started", Toast.LENGTH_LONG).show();
            return this.summary();
        }
        if(fullPathIndex >= fullPath.size()-2){
            Log.e("Skip_index", String.valueOf(fullPathIndex));

            Toast.makeText(context, "No more exhibit to skip!", Toast.LENGTH_LONG).show();
//            ArrayList<String> noMore = new ArrayList<>();
//            noMore.add("No more");
            return fullPath.get(fullPathIndex);
        }


        // Get current location
        String sourceID = sortedSelectedItemsIDs.get(fullPathIndex);
        Log.d("source:", sourceID);

        // Remove next location
        sortedSelectedItemsIDs.remove(fullPathIndex+1);
        int nextIndex = fullPathIndex+1;
        // Add rest locations except exit
        List<String> newSelectedItems = new ArrayList<>();
        for(int i = nextIndex; i < sortedSelectedItemsIDs.size() - 1; i++) {
            newSelectedItems.add(sortedSelectedItemsIDs.get(i));
        }

        ArrayList<String> leftID = sortID(newSelectedItems, sourceID);
        leftID.add("entrance_exit_gate");
        for(int i = 0; i < leftID.size(); i++){
            this.sortedSelectedItemsIDs.set(i+nextIndex, leftID.get(i));
        }
        leftID.add(0, sourceID);

        fullPath.remove(fullPath.size() - 1);

        int startIndex = 0;
        int goalIndex = 1;
        while(goalIndex < leftID.size()) {
            GraphPath<String, IdentifiedWeightedEdge> path = buildPath(leftID, startIndex, goalIndex);
            fullPath.set(fullPathIndex + startIndex, getDirections(path, goalIndex));
            goalIndex++;
            startIndex++;
        }
//        for(int i = nextIndex)
        //Log.d("index", String.valueOf(fullPathIndex));
        return fullPath.get(fullPathIndex);
    }


    public ArrayList<String> getDirections(GraphPath<String, IdentifiedWeightedEdge> path, int goalIndex) {
        ArrayList<String> directions = new ArrayList<>();
        int i = 1;

        Coord tmp = new Coord(vInfo.get(this.sortedSelectedItemsIDs.get(goalIndex)).lat, vInfo.get(this.sortedSelectedItemsIDs.get(goalIndex)).lng);
        this.hash.put(tmp.hashCode(), new ArrayList<String>());

        String defaultMessage = "Walk %.0f meters along %s between '%s' and '%s'.\n";
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            this.hash.get(tmp.hashCode()).add(vInfo.get(g.getEdgeSource(e)).id);

            directions.add(String.format(Locale.ENGLISH, defaultMessage,
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

    public ArrayList<String> sortID(List<String> tempSelectedItemsIDs, String sourceID) {
        if (sourceID == null){
            sourceID = "entrance_exit_gate";
        }
        String tempSource = "temp";
        double shortest = Double.MAX_VALUE;

        ArrayList<String> selected = new ArrayList<>();
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
            selected.add(sourceID);
            tempSelectedItemsIDs.remove(sourceID);
        }
        return selected;
    }

    public GraphPath<String, IdentifiedWeightedEdge> buildPath(List<String> sortedSelectedItemsIDs, int startIndex, int goalIndex) {
        String sourceItemID;

        sourceItemID = sortedSelectedItemsIDs.get(startIndex++);
        String goalID = sortedSelectedItemsIDs.get(goalIndex++);
        Log.d("Pathfinder", sourceItemID);
        Log.d("Pathfinder", goalID);
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, sourceItemID, goalID);
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            Log.d("Pathfinder", "E: " + e.toString());
        }
        return path;
    }

}
