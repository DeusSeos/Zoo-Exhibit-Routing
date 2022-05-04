package com.example.zooseeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class SelectedListAdapter extends ArrayAdapter<SearchListItem> {


    private LinkedHashSet<SearchListItem> selectedItems;

    public SelectedListAdapter(@NonNull Context context, int resource, LinkedHashSet<SearchListItem> selectedItems) {
        super(context, resource);
        this.selectedItems = selectedItems;
    }

    @Override
    public int getCount() {
        return selectedItems.size();
    }

    @Nullable
    @Override
    public SearchListItem getItem(int position) {
        //convert to arrayList
        ArrayList<SearchListItem> thing;
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


        SearchListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_view, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        name.setText(item.name);

        return convertView;


    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



}
