package com.example.zooseeker;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "search_list_items")
public class SearchListItem implements Parcelable {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String kind;
    public String name;
    public ArrayList<String> tags;
    public Double lat;
    public Double lng;


    public SearchListItem(@NonNull String id, @NonNull String kind, String name, ArrayList<String> tags, Double lat, Double lng) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
        this.lat = lat;
        this.lng = lng;
    }

    protected SearchListItem(Parcel in) {
        id = in.readString();
        kind = in.readString();
        name = in.readString();
        this.tags = new ArrayList<>();
        in.readList(tags, SearchListItem.class.getClassLoader() );
        lng = in.readDouble();
        lat = in.readDouble();
    }

    public static final Creator<SearchListItem> CREATOR = new Creator<SearchListItem>() {
        @Override
        public SearchListItem createFromParcel(Parcel in) {
            return new SearchListItem(in);
        }

        @Override
        public SearchListItem[] newArray(int size) {
            return new SearchListItem[size];
        }
    };

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
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(kind);
        parcel.writeString(name);
        parcel.writeList(tags);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);


    }
}
