package com.project.sewamobil.controller;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.project.sewamobil.model.Penyewa;

import java.util.List;

@Dao
public interface PenyewaDao {


    @Query("SELECT * FROM penyewa WHERE nama = :nama")  // Fetch by nama of penyewa
    LiveData<Penyewa> getPenyewaByNama(String nama);  // Fetch penyewa by nama

    @Query("SELECT * FROM penyewa")
    LiveData<List<Penyewa>> getAllPenyewa();  // Fetch all Penyewa

    @Query("DELETE FROM penyewa WHERE nama = :nama")  // Delete by nama
    void deletePenyewaByNama(String nama);  // Delete penyewa by nama

    @Insert
    void insert(Penyewa penyewa);  // Insert Penyewa

    @Insert
    long insertAndGetId(Penyewa penyewa);  // Returns the ID of the inserted Penyewa

    @Query("DELETE FROM penyewa WHERE id = :id")
    void deletePenyewaById(int id); // Tambahkan ini

    @Query("SELECT * FROM penyewa WHERE id = :penyewaId")
    LiveData<Penyewa> getPenyewaById(int penyewaId);

    @Query("SELECT * FROM penyewa WHERE id = :id")
    Penyewa getPenyewaByIdSync(int id);

    // Add query to check credentials for login (email and password)
    @Query("SELECT * FROM penyewa WHERE email = :email AND password = :password LIMIT 1")
    Penyewa getPenyewaByEmailAndPassword(String email, String password); // For login verification

    @Query("SELECT COUNT(*) FROM penyewa WHERE email = :email")
    int countByEmail(String email); // Check if email already exists

    @Query("UPDATE penyewa SET nama = :nama WHERE id = :id")
    void updateNamaById(int id, String nama);  // Update nama penyewa by ID

    @Query("UPDATE penyewa SET email = :email WHERE id = :id")
    void updateEmailById(int id, String email);  // Update email penyewa by ID

    @Query("UPDATE penyewa SET noHp = :phone WHERE id = :id")
    void updatePhoneById(int id, String phone);  // Update phone penyewa by ID

    @Query("UPDATE penyewa SET alamat = :alamat WHERE id = :id")
    void updateAlamatById(int id, String alamat);  // Update alamat penyewa by ID

    @Query("SELECT * FROM penyewa WHERE nama = :nama")
    Penyewa getPenyewaByNamaSync(String nama); // Versi sinkron


}
