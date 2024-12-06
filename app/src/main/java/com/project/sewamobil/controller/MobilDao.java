package com.project.sewamobil.controller;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.sewamobil.model.Mobil;

import java.util.List;
@Dao
public interface MobilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Mobil mobil);

    @Query("SELECT * FROM mobil")
    LiveData<List<Mobil>> getAllMobil();  // Retrieve all mobil records

    @Query("SELECT * FROM mobil WHERE make = :make")
    LiveData<Mobil> getMobilByMake(String make);  // Retrieve mobil by make

    @Query("SELECT * FROM mobil WHERE id = :mobilId LIMIT 1")
    LiveData<Mobil> getMobilById(int mobilId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Mobil> mobilList);  // Insert multiple mobil records

    @Query("SELECT make FROM mobil")
    LiveData<List<String>> getAllMakes();  // Fetch all car makes

    @Query("SELECT * FROM mobil")
    List<Mobil> getAllMobilSync();

    @Update
    void update(Mobil mobil);

    // Synchronous version
    @Query("SELECT * FROM mobil WHERE make = :make LIMIT 1")
    Mobil getMobilByMakeSync(String make);  // Synchronous version

    @Query("SELECT * FROM mobil WHERE id = :mobilId LIMIT 1")
    Mobil getMobilByIdSync(int mobilId);  // Synchronous version\

    @Query("SELECT * FROM mobil WHERE model = :model LIMIT 1")
    Mobil getMobilByModelSync(String model);
    @Query("SELECT * FROM mobil WHERE make LIKE '%' || :keyword || '%' OR model LIKE '%' || :keyword || '%'")
    LiveData<List<Mobil>> searchMobil(String keyword); // Asynchronous query

}
