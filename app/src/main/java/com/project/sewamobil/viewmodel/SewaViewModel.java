package com.project.sewamobil.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.sewamobil.db.AppDatabase;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.model.Penyewa;
import com.project.sewamobil.model.Sewa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SewaViewModel extends AndroidViewModel {

    private MutableLiveData<List<String>> categories; // To store car brands
    private final Executor executor;
    private final AppDatabase db;

    private final MutableLiveData<String> operationStatus; // To track operation status

    public SewaViewModel(Application application) {
        super(application);
        categories = new MutableLiveData<>();
        executor = Executors.newSingleThreadExecutor();
        db = AppDatabase.getDatabase(application);
        operationStatus = new MutableLiveData<>();
        loadCategories();
    }

    public LiveData<String> getOperationStatus() {
        return operationStatus;
    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }

    public LiveData<Mobil> getMobilByMerk(String merk) {
        // Mengakses DAO untuk mendapatkan mobil berdasarkan merk
        return AppDatabase.getDatabase(getApplication()).mobilDao().getMobilByMake(merk);
    }


    public void insert(String carModel, String renterName, String renterAddress, String renterPhone, int rentalDuration, int promoPercentage, double totalPrice) {
        executor.execute(() -> {
            try {
                // Fetch the car by its model
                Mobil mobil = db.mobilDao().getMobilByModelSync(carModel);
                if (mobil == null) {
                    operationStatus.postValue("Mobil tidak ditemukan.");
                    return;
                }

                // Check if a renter with the same name already exists (sync method)
                Penyewa existingPenyewa = db.penyewaDao().getPenyewaByNamaSync(renterName);
                long penyewaId;

                if (existingPenyewa != null) {
                    // Use the existing renter
                    penyewaId = existingPenyewa.getId();
                } else {
                    // Create a new Penyewa instance
                    Penyewa penyewa = new Penyewa(renterName, renterAddress, renterPhone);
                    penyewaId = db.penyewaDao().insertAndGetId(penyewa);
                }

                // Create a new Sewa instance with promo percentage
                Sewa sewa = new Sewa(mobil.getId(), (int) penyewaId, promoPercentage, rentalDuration, totalPrice);

                // Insert rental data into the database
                db.sewaDao().insert(sewa);

                // Update operation status
                operationStatus.postValue("Sewa berhasil disimpan");
            } catch (Exception e) {
                Log.e("SewaViewModel", "Error inserting sewa", e);
                operationStatus.postValue("Error: " + e.getMessage());
            }
        });
    }


    // Load car categories from the database
    private void loadCategories() {
        LiveData<List<Mobil>> mobilListLiveData = db.mobilDao().getAllMobil();
        mobilListLiveData.observeForever(mobilList -> {
            if (mobilList != null) {
                List<String> brandNames = new ArrayList<>();
                for (Mobil mobil : mobilList) {
                    if (!brandNames.contains(mobil.getMake())) {
                        brandNames.add(mobil.getMake());
                    }
                }
                categories.setValue(brandNames);
            }
        });
    }

    // Method to fetch all rentals by a specific renter name
    public LiveData<List<Sewa>> getSewaByNama(String nama) {
        return db.sewaDao().getSewaByNama(nama);
    }

    // Method to fetch all rental records
    public LiveData<List<Sewa>> getAllSewa() {
        return db.sewaDao().getAllSewa();
    }

    // Method to fetch car by its ID
    public LiveData<Mobil> getMobilById(int mobilId) {
        return db.mobilDao().getMobilById(mobilId);
    }

    // Method to fetch rentals by renter ID
    public LiveData<List<Sewa>> getSewaByPenyewaId(int penyewaId) {
        return db.sewaDao().getSewaByPenyewaId(penyewaId);
    }

    // Tambahkan metode di SewaViewModel
    public LiveData<List<Mobil>> getMobilByUserId(int userId) {
        return db.sewaDao().getMobilByUserId(userId);
    }




    // Clean up observers when the ViewModel is cleared
    @Override
    protected void onCleared() {
        super.onCleared();
        // Remove the observer to prevent memory leaks
        LiveData<List<Mobil>> mobilListLiveData = db.mobilDao().getAllMobil();
        mobilListLiveData.removeObserver(mobil -> {}
        );
    }

    public void deleteSewaByMobilId(int mobilId) {
        executor.execute(() -> {
            try {
                db.sewaDao().deleteByMobilId(mobilId);
                Log.d("SewaViewModel", "Item dengan mobilId: " + mobilId + " berhasil dihapus");
            } catch (Exception e) {
                Log.e("SewaViewModel", "Error saat menghapus item", e);
            }
        });
    }

    public void deleteSewaById(int sewaId) {
        executor.execute(() -> {
            try {
                db.sewaDao().deleteSewaById(sewaId);
                Log.d("SewaViewModel", "Sewa dengan id: " + sewaId + " berhasil dihapus");
            } catch (Exception e) {
                Log.e("SewaViewModel", "Error saat menghapus sewa", e);
            }
        });
    }


    // Tambahkan metode untuk mengambil Sewa berdasarkan sewaId
    public LiveData<Sewa> getSewaById(int sewaId) {
        return db.sewaDao().getSewaById(sewaId);
    }


}
