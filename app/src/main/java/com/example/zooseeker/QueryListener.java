package com.example.zooseeker;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;


//TODO: Pass in the search stuff and adapter stuff somehow
public class QueryListener implements SearchView.OnQueryTextListener {

    private final Context context;
    private final SearchListDao searchListDao;
    private final SearchListItemAdapter adapter;
    private final View listView;
    private final View selectedListView;

    public QueryListener(Context context, SearchListDao searchListDao, SearchListItemAdapter adapter, View listView, View selectedListView) {
        this.context = context;
        this.searchListDao = searchListDao;
        this.adapter = adapter;
        this.listView = listView;
        this.selectedListView = selectedListView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("SearchListActivity", query);
//                Log.d("SearchListActivity", animalNameList.toString());
        List<SearchListItem> nameList = searchListDao.getItemsByInput(query);
//                Log.d("SearchListActivity", nameList.toString());
        if (!nameList.isEmpty()) {
            adapter.getFilter().filter(query, new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int i) {
                    listView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Toast.makeText(context, "Unable to find location: " + query, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("SearchListActivity", query);
        if (TextUtils.isEmpty(query)) {
            listView.setVisibility(View.GONE);
            selectedListView.setVisibility(View.VISIBLE);
        } else {
            adapter.getFilter().filter(query, i -> {
                listView.setVisibility(View.VISIBLE);
                selectedListView.setVisibility(View.GONE);
            });
        }
        return false;

    }
}


