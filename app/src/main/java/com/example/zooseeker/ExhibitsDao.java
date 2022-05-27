package com.example.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ExhibitsDao {
    @Insert
    void insert(Exhibit... exhibits);

    @Insert
    void insert(List<Exhibit> exhibits);

    @Query("SELECT COUNT(*) from exhibits")
    long count();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    List<ExhibitWithGroup> getExhibitsWithGroups();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    LiveData<List<ExhibitWithGroup>> getExhibitsWithGroupsLive();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE id=:id")
    ExhibitWithGroup getExhibitWithGroupById(String id);
}
