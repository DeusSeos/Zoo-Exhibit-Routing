package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedHashSet;

public class DirectionActivity extends AppCompatActivity {

    private ListView directionList;
    private Button nextButton;
    LinkedHashSet<SearchListItem> selectedItems = (LinkedHashSet<SearchListItem>) getIntent().getSerializableExtra("selected_list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        directionList = findViewById(R.id.direction_list);
        nextButton = findViewById(R.id.next_button);

        //generate the graph


        //create a new list view
        //parse the locations that are selected and run dijkstra between each node
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