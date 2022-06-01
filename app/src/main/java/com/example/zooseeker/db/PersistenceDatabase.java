package com.example.zooseeker.db;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(entities = {ID.class}, version = 1, exportSchema = false)
public abstract class PersistenceDatabase extends RoomDatabase {

    private static PersistenceDatabase singleton = null;

    public abstract IDDao IDDao();

    public synchronized static PersistenceDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = PersistenceDatabase.makeDatabase(context);
        }
        return singleton;
    }

    public static void populate(PersistenceDatabase instance, List<ID> idList){
        Log.i(PersistenceDatabase.class.getCanonicalName(), "Populating database from selected exhibit list...");
        instance.IDDao().insert(idList);
    }

    public static void clear(PersistenceDatabase instance){
        Log.i(PersistenceDatabase.class.getCanonicalName(), "Clearing database of exhibits...");
        instance.IDDao().deleteIds();
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
        return singleton.IDDao().count() > 0;
    }


    @VisibleForTesting
    public static void injectTestDatabase(PersistenceDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }

        singleton = testDatabase;
    }


}
