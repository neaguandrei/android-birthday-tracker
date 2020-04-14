package com.aneagu.birthdaytracker.data.repository.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BirthdayDao {

    @Insert
    void save(Birthday... birthdays);

    @Delete
    void delete(Birthday birthday);

    @Query("SELECT * FROM birthday")
    List<Birthday> findAll();

    @Query("SELECT * FROM birthday WHERE full_name LIKE :name || '%'")
    List<Birthday> findAllByName(String name);
}
