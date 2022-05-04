package com.example.zooseeker;

import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchListItemAdapter extends ArrayAdapter<SearchListItem>  implements Filterable {

    private List<SearchListItem> originalItems;
    private List<SearchListItem> filteredItems;
    private Filter itemFilter = new ItemFilter();
    private LayoutInflater inflater;

    public SearchListItemAdapter(@NonNull Context context, List<SearchListItem> items) {
        super(context, 0, items);
        this.originalItems = items;
        this.filteredItems = items;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Nullable
    @Override
    public SearchListItem getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SearchListItem item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_view, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        name.setText(item.name);

        return convertView;

    }

    static class ViewHolder {
        TextView text;
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
            final List<SearchListItem> list = originalItems;
            int count = list.size();
            final ArrayList<SearchListItem> listItems = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                SearchListItem searchItem = list.get(i);
                filterableString = searchItem.name;
                if (filterableString.toLowerCase().startsWith(filterString)) {
                    Log.d("Filter", "adding: " + filterableString);
                    listItems.add(searchItem);
                }
            }

            results.values = listItems;
            results.count = listItems.size();

            Log.d("Filter", constraint.toString() + listItems.toString());

            return results;

        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            filteredItems = (ArrayList<SearchListItem>) results.values;
            notifyDataSetChanged();

        }
    }
}
