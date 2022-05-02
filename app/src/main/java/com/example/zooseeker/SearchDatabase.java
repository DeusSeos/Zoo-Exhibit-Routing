package com.example.zooseeker;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {SearchListItem.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class SearchDatabase extends RoomDatabase {

    private static SearchDatabase singleton = null;

    public abstract SearchListDao searchListDao();

    public synchronized static SearchDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = SearchDatabase.makeDatabase(context);
    }
        return singleton;

    }

    private static SearchDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, SearchDatabase.class, "zooseeker.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<SearchListItem> todos = SearchListItem.loadJson(context, "sample_node_info.json");
                            getSingleton(context).searchListDao().insertAll(todos);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(SearchDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }

        singleton = testDatabase;
    }

}
