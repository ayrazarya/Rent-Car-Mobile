package com.project.sewamobil.util;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Kelas ini digunakan untuk mengonversi objek menggunakan Gson.
 */
public class GsonConverter {

    // Mengonversi List<String> menjadi JSON String
    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(list);  // Mengonversi List<String> menjadi JSON String
    }

    // Mengonversi JSON String menjadi List<String>
    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(value, listType);  // Mengonversi JSON String kembali menjadi List<String>
    }
}
