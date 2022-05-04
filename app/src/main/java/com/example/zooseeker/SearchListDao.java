package com.example.zooseeker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT `name` FROM search_list_items ORDER BY `name`")
    List<String> getByName();


    @Query("SELECT * FROM `search_list_items` ORDER BY `id`")
    List<SearchListItem> getAll();

    @Update
    int update(SearchListItem searchListItem);

    @Delete
    int delete(SearchListItem searchListItem);

    @Query("DELETE FROM `search_list_items`")
    void deleteAll();

    @Query("SELECT `name` FROM search_list_items WHERE tags LIKE '%' || :tag || '%' ORDER by name ")
    List<String> getByTag(String tag);

    @Query("SELECT `name` FROM search_list_items WHERE tags LIKE '%' || :in || '%' OR name = :in ORDER by name ")
    List<String> getByInput(String in);

    @Query("SELECT * FROM `search_list_items` WHERE tags LIKE '%' || :in || name = :in ORDER by name")
    List<SearchListItem> getItemsByInput(String in);
}