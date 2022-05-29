package com.example.zooseeker.db;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(entities = {Exhibit.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class PersistenceDatabase extends RoomDatabase {

    private static PersistenceDatabase singleton = null;

    public abstract ExhibitsDao exhibitsDao();

    public synchronized static PersistenceDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = PersistenceDatabase.makeDatabase(context);
        }
        return singleton;

    }

    public static void populate(PersistenceDatabase instance, List<Exhibit> exhibitList){
        Log.i(PersistenceDatabase.class.getCanonicalName(), "Populating database from selected exhibit list...");


    }

    private static PersistenceDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, PersistenceDatabase.class, "Persistance.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                })
                .build();
    }


    private static boolean isPopulated() {
        return singleton.exhibitsDao().count() > 0;
    }


    @VisibleForTesting
    public static void injectTestDatabase(PersistenceDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }

        singleton = testDatabase;
    }


}
