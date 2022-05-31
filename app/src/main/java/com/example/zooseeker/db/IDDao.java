package com.example.zooseeker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IDDao {

    @Insert
    void insert(ID... ids);

    @Insert
    void insert(List<ID> idList);

    @Query("SELECT COUNT(*) from id")
    long count();

    @Query("DELETE FROM id")
    void deleteIds();

    @Query("SELECT * FROM id")
    List<ID> getIds();

}
