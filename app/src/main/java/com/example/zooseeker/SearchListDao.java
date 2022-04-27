package com.example.zooseeker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SearchListDao {

    // insert method receives a single parameter, it can return a long value, which is the new rowId for the inserted item
    @Insert
    long insert(SearchListItem searchListItem);

    @Insert
    List<Long> insertAll(List<SearchListItem> searchListItems);

    @Query("SELECT * FROM `search_list_items` WHERE `id`=:id")
    SearchListItem get(String id);

    @Query("SELECT * FROM `search_list_items` ORDER BY `id`")
    List<SearchListItem> getAll();


}
