package com.project.sewamobil.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// Penyewa entity using ID as primary key
@Entity(tableName = "penyewa")
public class Penyewa {

    @PrimaryKey(autoGenerate = true)  // Auto-generate ID for Penyewa
    private int id;  // Unique identifier
    private String nama; // Name of the person
    private String alamat; // Address of the person
    private String noHp;   // Phone number
    private String password; // Password of the person
    private String email;

    public Penyewa(String nama, String alamat, String noHp, String password, String email) {
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
        this.password = password;
        this.email = email;
    }

    @Ignore
    public Penyewa(String nama, String alamat, String noHp) {
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;

    }



    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
