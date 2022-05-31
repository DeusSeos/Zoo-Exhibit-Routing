package com.example.zooseeker;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zooseeker.db.ExhibitWithGroup;

import java.util.ArrayList;
import java.util.List;

public class DirectionActivity extends AppCompatActivity {

    private Pathfinder pathy;
    private ListView directionList;
    private ArrayList<ExhibitWithGroup> selectedItems;

    List<String> directionsArray = new ArrayList<>();
    private ArrayAdapter<String> directionsAdapter;

    public DirectionActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        // Initialize variables
        directionList = findViewById(R.id.direction_list);
        Button nextButton = findViewById(R.id.next_button);
        Button backButton = findViewById(R.id.back_button);
        Button skipButton = findViewById(R.id.skip_button);
        Button mockButton = findViewById(R.id.mock_button);

        EditText mockLocation = findViewById(R.id.location);
//        ImageButton settingsButton = findViewById(R.id.settings_button);

        // Try to load the selected items list from previous activity
        if (getIntent().getParcelableArrayListExtra("selected_list") != null) {
            selectedItems = getIntent().getParcelableArrayListExtra("selected_list");
            Log.d("DirectionActivity", "Loaded arraylist from extra: " + selectedItems.toString());
        } else {
            Log.d("DirectionActivity", "Oopsie loading broke");
        }

        pathy = new Pathfinder(this, selectedItems);
        // could make this a call in the constructor (depends if we want to always optimize path first or not)
        pathy.optimizeSelectedItemsIDs(null);

        directionsArray = pathy.summary();

        //Create array to loop directions into
        directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
        directionList.setAdapter(directionsAdapter);


        //edit nextButton onClick
        nextButton.setOnClickListener(view -> {
            directionsArray = pathy.next();
            Log.d("DirectionActivity", "New directions: " + directionsArray.toString());
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
        });

        backButton.setOnClickListener(view -> {
            directionsArray = pathy.back();
            Log.d("DirectionActivity", "Previous directions: " + directionsArray.toString());
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
        });


        skipButton.setOnClickListener(view -> {
            directionsArray = pathy.skip();
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
        });


        mockButton.setOnClickListener(view -> {
            String s = mockLocation.getText().toString();
            int flag = pathy.mock(s);
            Log.d("flag", String.valueOf(flag));
            if (flag == -1){

            } else {
                directionsArray = pathy.update(flag);
                directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
                directionList.setAdapter(directionsAdapter);
            }
        });
    }

}
