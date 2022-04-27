package com.example.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "search_list_items")
public class SearchListItem {

    @PrimaryKey(autoGenerate = false)
    public String id;

    @NonNull
    public String itemType;
    public List<String> tags;


    public SearchListItem(String id, @NonNull String itemType, List<String> tags) {
        this.id = id;
        this.itemType = itemType;
        this.tags = tags;
    }

    public static List<SearchListItem> loadJson(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<SearchListItem>>() {
            }.getType();
            return gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "SearchListItem{" +
                "id='" + id + '\'' +
                ", itemType='" + itemType + '\'' +
                ", tags=" + tags +
                '}';
    }
}
