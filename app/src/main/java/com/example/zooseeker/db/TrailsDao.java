package com.example.zooseeker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrailsDao {
    @Insert
    void insert(Trail... trail);

    @Insert
    void insert(List<Trail> trails);

    @Query("SELECT COUNT(*) from trails")
    long count();
}
