package com.project.sewamobil.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Entitas Sewa untuk tabel dalam database Room.
 *
 */

@Entity(
        tableName = "sewa",
        foreignKeys = {
                @ForeignKey(entity = Mobil.class, parentColumns = "id", childColumns = "mobilId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Penyewa.class, parentColumns = "id", childColumns = "penyewaId", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "mobilId", name = "index_mobil_id"),
                @Index(value = "penyewaId", name = "index_penyewa_id")
        }
)
public class Sewa {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "mobilId")
    private int mobilId;

    @ColumnInfo(name = "penyewaId")
    private int penyewaId;

    private int promo;
    private int lama;
    private double total;

    // Constructor default diperlukan untuk Room
    // Constructor dengan parameter
    public Sewa(int mobilId, int penyewaId, int promo, int lama, double total) {

        this.mobilId = mobilId;
        this.penyewaId = penyewaId;
        this.promo = promo;
        this.lama = lama;
        this.total = total;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMobilId() {
        return mobilId;
    }

    public void setMobilId(int mobilId) {
        this.mobilId = mobilId;
    }

    public int getPenyewaId() {
        return penyewaId;
    }

    public void setPenyewaId(int penyewaId) {
        this.penyewaId = penyewaId;
    }

    public int getPromo() {
        return promo;
    }

    public void setPromo(int promo) {
        this.promo = promo;
    }

    public int getLama() {
        return lama;
    }

    public void setLama(int lama) {
        this.lama = lama;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


}
