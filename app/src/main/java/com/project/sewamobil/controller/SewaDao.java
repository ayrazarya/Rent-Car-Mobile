package com.project.sewamobil.controller;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.model.Sewa;

import java.util.List;
@Dao
public interface SewaDao {

    @Query("SELECT * FROM sewa WHERE penyewaId = :penyewaId")
    List<Sewa> getSewaByPenyewaIdSync(int penyewaId); // Versi sinkron


    @Query("SELECT * FROM sewa WHERE penyewaId = :penyewaId")
    LiveData<List<Sewa>> getSewaByPenyewaId(int penyewaId);

    @Insert
    void insert(Sewa sewa);

    @Query("SELECT * FROM sewa")
    LiveData<List<Sewa>> getAllSewa();  // Retrieve all sewa records

    @Query("SELECT * FROM sewa WHERE mobilId = :mobilId AND penyewaId = :penyewaId")
    LiveData<Sewa> getSewaByMobilAndPenyewa(int mobilId, int penyewaId); // Fetch sewa by mobilId and penyewaId

    @Query("SELECT * FROM sewa INNER JOIN penyewa ON sewa.penyewaId = penyewa.id WHERE penyewa.nama = :nama")
    LiveData<List<Sewa>> getSewaByNama(String nama); // Fetch sewa by penyewa's nama


    @Query("SELECT m.* FROM mobil m INNER JOIN sewa s ON m.id = s.mobilId WHERE s.penyewaId = :penyewaId")
    LiveData<List<Mobil>> getMobilByPenyewaId(int penyewaId);

    @Query("DELETE FROM sewa WHERE mobilId = :mobilId")
    void deleteByMobilId(int mobilId);


    @Query("SELECT * FROM sewa WHERE mobilId = :mobilId")
    LiveData<List<Sewa>> getSewaByMobilId(int mobilId);

    @Query("SELECT * FROM Mobil INNER JOIN Sewa ON Mobil.id = Sewa.mobilid WHERE Sewa.penyewaid = :userId")
    LiveData<List<Mobil>> getMobilByUserId(int userId);



    @Query("SELECT * FROM sewa WHERE id = :id")
    LiveData<Sewa> getSewaById(int id); // Mengambil sewa berdasarkan id

    @Query("DELETE FROM sewa WHERE id = :sewaId")
    void deleteSewaById(int sewaId);

}
