package com.example.zooseeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zooseeker.R;
import com.example.zooseeker.db.Exhibit;
import com.example.zooseeker.db.ExhibitWithGroup;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class SelectedExhibitAdapter extends ArrayAdapter<ExhibitWithGroup> {


    private LinkedHashSet<ExhibitWithGroup> selectedItems;

    public SelectedExhibitAdapter(@NonNull Context context, int resource, LinkedHashSet<ExhibitWithGroup> selectedItems) {
        super(context, resource);
        this.selectedItems = selectedItems;
    }

    @Override
    public int getCount() {
        return selectedItems.size();
    }

    @Nullable
    @Override
    public ExhibitWithGroup getItem(int position) {
        //convert to arrayList
        ArrayList<ExhibitWithGroup> thing;
        thing = new ArrayList<>(selectedItems);
        return thing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ExhibitWithGroup item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_view, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        name.setText(item.exhibit.name);

        return convertView;


    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



}
