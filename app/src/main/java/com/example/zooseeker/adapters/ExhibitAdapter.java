package com.example.zooseeker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zooseeker.R;
import com.example.zooseeker.db.Exhibit;
import com.example.zooseeker.db.ExhibitWithGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ExhibitAdapter extends ArrayAdapter<ExhibitWithGroup>  implements Filterable {

    private List<ExhibitWithGroup> originalItems;
    private List<ExhibitWithGroup> filteredItems;
    private Filter itemFilter = new ItemFilter();


    public ExhibitAdapter(@NonNull Context context, List<ExhibitWithGroup> items) {
        super(context, 0);
        this.originalItems = items;
        this.filteredItems = items;
    }

    public void setItems(List<ExhibitWithGroup> items) {
        this.originalItems = items;
        this.filteredItems = items;
    }


    @Override
    public void remove(@Nullable ExhibitWithGroup object) {
        originalItems.remove(object);
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Nullable
    @Override
    public ExhibitWithGroup getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ExhibitWithGroup item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_view, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        name.setText(item.exhibit.name);

        return convertView;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase(Locale.ROOT);

            FilterResults  results = new FilterResults();
            final List<ExhibitWithGroup> list = originalItems;
            int count = list.size();
            final ArrayList<ExhibitWithGroup> listItems = new ArrayList<>(count);

            String filterableString;
            List<String> filterableTags;
            String filterableKind;

            if (constraint.equals("all")){
                results.values = list;
                return results;
            }

            for (int i = 0; i < count; i++) {
                ExhibitWithGroup searchItem = list.get(i);

                filterableString = searchItem.exhibit.name;
                filterableTags = searchItem.exhibit.tags;
//                filterableKind = Exhibit.Kind.valueOf(searchItem.kind);
                boolean tagsContained = filterableTags.stream()
                        .filter(element -> element.contains(filterString))
                        .collect(Collectors.toList()).size() > 0;
//                boolean exhibitContained = filterableKind.equals(filterString);
                if (filterableString.toLowerCase().startsWith(filterString) || tagsContained) {
                    Log.d("Filter", "adding: " + filterableString);
                    listItems.add(searchItem);
                }
            }


            results.values = listItems;
            results.count = listItems.size();

            Log.d("Filter", constraint + listItems.toString());

            return results;

        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            filteredItems = (ArrayList<ExhibitWithGroup>) results.values;
            notifyDataSetChanged();

        }
    }
}
