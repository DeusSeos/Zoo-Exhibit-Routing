package com.example.zooseeker.db;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Exhibit.class, Trail.class}, version = 1, exportSchema = false)
@TypeConverters({SearchDatabase.Converters.class})
public abstract class SearchDatabase extends RoomDatabase {

    private static SearchDatabase singleton = null;
    private static boolean shouldForcePopulate = false;

    public abstract ExhibitsDao exhibitsDao();

    public abstract TrailsDao trailsDao();

    public synchronized static SearchDatabase getDatabase(Context context) {
        if (singleton == null) {
            Log.d("Database", "YO YO YO");
            singleton = SearchDatabase.makeDatabase(context);
        }
        return singleton;

    }

    private static SearchDatabase makeDatabase(Context context) {
        Log.d("Database", "making database");
//        Log.d("Database", "Populated " + String.valueOf(isPopulated()));
        Log.d("Database", "Force POp " + String.valueOf(shouldForcePopulate));
        return Room.databaseBuilder(context, SearchDatabase.class, "zooseeker.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            // Don't populate on open unless the database is empty.
                            if (isPopulated() && !shouldForcePopulate) return;
//                            if (shouldForcePopulate) return;
                            Log.i("Database",
                                    "Database is empty or re-popualtion forced, populating...");
                            populate(context, singleton);
                        });
                    }

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            Log.i("Database",
                                    "Database is new, populating...");
                            populate(context, singleton);
                        });
                    }
                })
                .build();
    }

    public static void populate(Context context, SearchDatabase instance) {
        Reader exhibitsReader = null;
        Reader trailsReader = null;
        try {
            exhibitsReader = new InputStreamReader(context.getAssets().open("exhibit_info.json"));
            trailsReader = new InputStreamReader(context.getAssets().open("trail_info.json"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data for prepopulation!");
        }
        populate(context, instance, exhibitsReader, trailsReader);
    }

    @VisibleForTesting
    public static void populate(Context context, SearchDatabase instance, Reader exhibitsReader, Reader trailsReader) {
        Log.i("Database", "Populating database from assets...");

        // Clear all of the tables
        instance.clearAllTables();
        shouldForcePopulate = false;

        var exhibits = Exhibit.fromJson(exhibitsReader);
        instance.exhibitsDao().insert(exhibits);

        var trails = Trail.fromJson(trailsReader);
        instance.trailsDao().insert(trails);
    }


    private static boolean isPopulated() {
        return singleton.exhibitsDao().count() > 0
                && singleton.trailsDao().count() > 0;
    }

    public static void setForcePopulate() {
        shouldForcePopulate = true;
    }


    @VisibleForTesting
    public static void injectTestDatabase(SearchDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }

        singleton = testDatabase;
    }


    public static class Converters {
        //        @TypeConverter
//        public static List<String> fromCsv(String csv) {
//            return List.of(",".split(csv));
//        }
//
//        @TypeConverter
//        public static String toCsv(List<String> tags) {
//            return String.join(",", tags);
//        }
        @TypeConverter
        public static ArrayList<String> fromString(String value) {
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String fromStringArray(ArrayList<String> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }

}
