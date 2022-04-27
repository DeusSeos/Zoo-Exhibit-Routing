package com.example.zooseeker;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SearchListItem.class}, version = 1)
public abstract class SearchDatabase extends RoomDatabase {
    public abstract SearchListDao searchListDao();







}
