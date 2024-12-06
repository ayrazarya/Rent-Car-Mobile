package com.project.sewamobil.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.project.sewamobil.controller.MobilDao;
import com.project.sewamobil.controller.PenyewaDao;
import com.project.sewamobil.controller.SewaDao;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.model.Penyewa;
import com.project.sewamobil.model.Sewa;
import com.project.sewamobil.util.GsonConverter;

import java.util.Arrays;


@Database(entities = {Mobil.class , Penyewa.class, Sewa.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MobilDao mobilDao();

    public abstract PenyewaDao penyewaDao();

    public abstract SewaDao sewaDao();

    @TypeConverters({GsonConverter.class})  // Menggunakan GsonConverter di sini
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "rentalmobil.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Tambahkan data awal saat database dibuat
                                    databaseWriteExecutor.execute(() -> {
                                        AppDatabase database = getDatabase(context);
                                        MobilDao mobilDao = database.mobilDao();

                                        // Data awal
                                        List<Mobil> initialMobils = Arrays.asList(
                                                new Mobil("Toyota", "Corolla", 2022, "Silver", 20000, 25000,
                                                        "Gasoline", "Automatic", "2.0L 4-cylinder", 169,
                                                        Arrays.asList("Bluetooth", "Backup Camera", "Keyless Entry"),
                                                        1,
                                                        "https://imgcdn.oto.com/large/gallery/color/38/2227/toyota-corolla-altis-color-121900.jpg"),
                                                new Mobil("Honda", "Civic", 2021, "White", 18000, 22000,
                                                        "Gasoline", "CVT", "1.5L 4-cylinder", 158,
                                                        Arrays.asList("Apple CarPlay", "Android Auto", "Lane Departure Warning"),
                                                        2,
                                                        "https://hondamobiltangerang.com/wp-content/uploads/2023/07/Featured-Image-Honda-Civic-1024x577.png"),
                                                new Mobil("Ford", "Mustang", 2020, "Red", 15000, 35000,
                                                        "Gasoline", "Manual", "5.0L V8", 450,
                                                        Arrays.asList("Leather Seats", "Navigation System", "Heated Seats"),
                                                        1,
                                                        "https://cdn.pixabay.com/photo/2017/09/20/00/28/ford-mustang-2767124_1280.png"),
                                                new Mobil("Chevrolet", "Equinox", 2019, "Blue", 30000, 20000,
                                                        "Gasoline", "Automatic", "1.5L 4-cylinder", 170,
                                                        Arrays.asList("Rearview Camera", "Blind-Spot Monitoring", "Wi-Fi Hotspot"),
                                                        2,
                                                        "https://inv.assets.ansira.net/RTT/Chevrolet/2025/6251203/default/ext_GAZ_deg02.jpg"),
                                                new Mobil("Nissan", "Altima", 2020, "Black", 22000, 21000,
                                                        "Gasoline", "CVT", "2.5L 4-cylinder", 188,
                                                        Arrays.asList("Remote Start", "Automatic Emergency Braking", "Lane Keeping Assist"),
                                                        1,
                                                        "https://vehicle-images.dealerinspire.com/stock-images/chrome/cf2137d64cf96bd2f6e00749abe83a36.png"),
                                                new Mobil("BMW", "3 Series", 2021, "White", 12000, 40000,
                                                        "Diesel", "Automatic", "2.0L 4-cylinder", 255,
                                                        Arrays.asList("Sunroof", "Adaptive Cruise Control", "Parking Assistance"),
                                                        1,
                                                        "https://m.atcdn.co.uk/a/media/w600/6c489624cb6a4d1a9de8fa65ff6d90ac.jpg"),
                                                new Mobil("Tesla", "Model 3", 2022, "Red", 8000, 45000,
                                                        "Electric", "Automatic", "Electric Motor", 322,
                                                        Arrays.asList("Autopilot", "Full Self-Driving Capability", "Premium Audio System"),
                                                        1,
                                                        "https://cdn.prod.website-files.com/60ce1b7dd21cd517bb39ff20/6153cdf8aec0a73b65af24c0_tesla-model-3.png"),
                                                new Mobil("Audi", "Q5", 2021, "Gray", 15000, 38000,
                                                        "Gasoline", "Automatic", "2.0L 4-cylinder", 248,
                                                        Arrays.asList("Virtual Cockpit", "Panoramic Sunroof", "Apple CarPlay"),
                                                        1,
                                                        "https://imgd.aeplcdn.com/642x336/n/cw/ec/53591/q5-exterior-right-front-three-quarter-36.jpeg?isig=0&q=80"),
                                                new Mobil("Mercedes-Benz", "E-Class", 2020, "Silver", 18000, 42000,
                                                        "Gasoline", "Automatic", "2.0L 4-cylinder", 255,
                                                        Arrays.asList("MBUX Infotainment", "Heated Steering Wheel", "Air Suspension"),
                                                        2,
                                                        "https://img.cintamobil.com/2021/10/20/cWC5a43e/e-class-9392.png"),
                                                new Mobil("Chevrolet", "Tahoe", 2020, "Black", 25000, 45000,
                                                        "Gasoline", "Automatic", "5.3L V8", 355,
                                                        Arrays.asList("Leather Seats", "Third-Row Seating", "Power Liftgate"),
                                                        2,
                                                        "https://inv.assets.ansira.net/ChromeColorMatch/us/TRANSPARENT_cc_2025CHS111923155_01_1280_GAZ.png")
                                                // ... (add all other data similarly)
                                        );

                                        // Insert all data
                                        mobilDao.insertAll(initialMobils);
                                        Log.d("AppDatabase", "Semua data berhasil dimasukkan.");
                                    });
                                }



                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    // Aktifkan foreign keys
                                    db.execSQL("PRAGMA foreign_keys=ON;");
                                    Log.d("AppDatabase", "Foreign keys activated.");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}