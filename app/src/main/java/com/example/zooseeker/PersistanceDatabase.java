package com.example.zooseeker;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Exhibit.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class PersistanceDatabase extends RoomDatabase {

    private static PersistanceDatabase singleton = null;

    public abstract SearchListDao searchListDao();

    public synchronized static PersistanceDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = PersistanceDatabase.makeDatabase(context);
        }
        return singleton;

    }

    private static SearchDatabase makeDatabase(Context context, List<Exhibit> exhibitList) {
        return Room.databaseBuilder(context, SearchDatabase.class, "Persistance.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            getSingleton(context).searchListDao().insertAll(exhibitList);
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
