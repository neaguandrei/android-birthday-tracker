package com.aneagu.birthdaytracker.data.repository.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aneagu.birthdaytracker.data.repository.models.Birthday;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface BirthdayDao {

    @Insert
    void save(Birthday... birthdays);

    @Query("DELETE FROM birthday WHERE full_name LIKE :fullName")
    void delete(String fullName);

    @Update
    void update(Birthday birthday);

    @Query("SELECT * FROM birthday")
    List<Birthday> findAll();

    @Query("SELECT * FROM birthday WHERE full_name LIKE :fullName || '%' AND deleted_on IS NULL")
    List<Birthday> findAllByName(String fullName);

    @Query("SELECT * FROM birthday WHERE deleted_on IS NULL")
    List<Birthday> findAllExistingBirthdays();
}
