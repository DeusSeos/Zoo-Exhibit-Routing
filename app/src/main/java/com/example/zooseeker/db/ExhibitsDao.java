package com.example.zooseeker.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.zooseeker.SearchListItem;

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


    @Query("SELECT `name` FROM exhibits ORDER BY `name` ASC")
    List<ExhibitWithGroup> getByName();

    @Query("SELECT `name` FROM exhibits WHERE tags LIKE '%' || :in || '%' OR name = :in ORDER by name ")
    List<String> getByInput(String in);

    @Query("SELECT * FROM `exhibits` WHERE tags LIKE '%' || :in || name = :in ORDER by name")
    List<SearchListItem> getExhibitsByInput(String in);


}
