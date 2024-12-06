package com.project.sewamobil.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.project.sewamobil.api.ApiService;
import com.project.sewamobil.db.AppDatabase;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.controller.MobilDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MobilViewModel extends AndroidViewModel {
    private final LiveData<List<Mobil>> allMobil;

    public MobilViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        allMobil = database.mobilDao().getAllMobil();
    }

    public LiveData<List<Mobil>> getAllMobil() {
        return allMobil;
    }



    public LiveData<Mobil> getMobilByMake(String make) {
        return AppDatabase.getDatabase(getApplication()).mobilDao().getMobilByMake(make);
    }

    public void syncDataFromApi(ApiService apiService) {
        apiService.getCars().enqueue(new Callback<List<Mobil>>() {
            @Override
            public void onResponse(Call<List<Mobil>> call, Response<List<Mobil>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MobilViewModel", "Data received: " + response.body().toString());

                    // Proses data yang diterima
                    List<Mobil> filteredList = new ArrayList<>();
                    for (Mobil mobil : response.body()) {
                        // Set make and color default if null
                        if (mobil.getMake() == null || mobil.getMake().isEmpty()) {
                            mobil.setMake("Unknown make");
                        }
                        if (mobil.getColor() == null || mobil.getColor().isEmpty()) {
                            mobil.setColor("Unknown color");
                        }
                        filteredList.add(mobil);
                    }

                    // Insert data into database
                    new Thread(() -> AppDatabase.getDatabase(getApplication())
                            .mobilDao()
                            .insertAll(filteredList)).start();
                } else {
                    Log.e("MobilViewModel", "Failed to retrieve data or data is empty");
                }
            }

            @Override
            public void onFailure(Call<List<Mobil>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<Mobil> getMobilById(int mobilId) {
        // Use AppDatabase to get the MobilDao and call getMobilById
        return AppDatabase.getDatabase(getApplication()).mobilDao().getMobilById(mobilId);
    }



    public LiveData<Mobil> getMobilByMerk(String merk) {
        return AppDatabase.getDatabase(getApplication()).mobilDao().getMobilByMake(merk);  // Use getMobilByMake
    }


    public LiveData<List<Mobil>> searchMobil(String keyword) {
        return AppDatabase.getDatabase(getApplication()).mobilDao().searchMobil(keyword);
    }

}