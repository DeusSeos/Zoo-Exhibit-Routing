package com.example.zooseeker.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("DELETE FROM exhibits")
    void deleteExhibits();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    List<ExhibitWithGroup> getExhibitsWithGroups();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    LiveData<List<ExhibitWithGroup>> getExhibitsWithGroupsLive();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE id=:id")
    ExhibitWithGroup getExhibitWithGroupById(String id);

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    List<ExhibitWithGroup> getExhibits();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' || tags LIKE '%' || :in || name = :in ORDER BY name ASC")
    List<ExhibitWithGroup> getExhibitsByName(String in);


}
