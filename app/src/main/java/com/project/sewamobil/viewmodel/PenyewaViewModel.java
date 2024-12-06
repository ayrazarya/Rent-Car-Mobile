package com.project.sewamobil.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.sewamobil.db.AppDatabase;
import com.project.sewamobil.model.Penyewa;
import com.project.sewamobil.model.Sewa;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class PenyewaViewModel extends AndroidViewModel {

    private final Executor executor;
    private final AppDatabase db;
    private final ExecutorService executorService;
    private final MutableLiveData<Penyewa> penyewaLiveData = new MutableLiveData<>();

    public PenyewaViewModel(Application application) {
        super(application);
        executor = Executors.newSingleThreadExecutor();
        db = AppDatabase.getDatabase(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    // Fetch Penyewa by nama
    public LiveData<Penyewa> getPenyewaByNama(String nama) {
        return db.penyewaDao().getPenyewaByNama(nama);  // Fetch by nama now
    }

    public LiveData<String> registerUser(String email, String password) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            if (!email.contains("@")) { // Validasi email harus mengandung '@'
                result.postValue("Pleas use a valid email");
                return;
            }

            if (password.length() < 8) { // Validasi panjang password minimal 8 karakter
                result.postValue("Password must be at least 8 characters");
                return;
            }

            int count = db.penyewaDao().countByEmail(email); // Periksa apakah email sudah ada
            if (count > 0) {
                result.postValue("Email already exists");
            } else {
                // Jika validasi berhasil, buat objek Penyewa baru dan simpan ke database
                Penyewa penyewa = new Penyewa(null, null, null, password, email);
                db.penyewaDao().insert(penyewa);
                result.postValue("Registration successful!");
            }
        });
        return result;
    }

    public void loginUser(String email, String password) {
        executorService.execute(() -> {
            Penyewa penyewa = db.penyewaDao().getPenyewaByEmailAndPassword(email, password);
            penyewaLiveData.postValue(penyewa);
        });
    }

    public LiveData<Penyewa> getPenyewaLiveData() {
        return penyewaLiveData;
    }


    public LiveData<List<Sewa>> getSewaByPenyewaId(int penyewaId) {
        return db.sewaDao().getSewaByPenyewaId(penyewaId);
    }


    // Menambahkan metode untuk mendapatkan penyewa berdasarkan ID
    public LiveData<Penyewa> getPenyewaById(int penyewaId) {
        return db.penyewaDao().getPenyewaById(penyewaId);
    }

    // Delete Penyewa by nama
    public void deletePenyewaByNama(String nama) {
        executor.execute(() -> db.penyewaDao().deletePenyewaByNama(nama));
    }

    public void updateUserAddress(int userId, String newAddress) {
        executor.execute(() -> db.penyewaDao().updateAlamatById(userId, newAddress)); // Execute on background thread
    }



    // Get all Penyewa
    public LiveData<List<Penyewa>> getAllPenyewa() {
        return db.penyewaDao().getAllPenyewa();
    }

    public void deletePenyewaById(int id) {
        executor.execute(() -> db.penyewaDao().deletePenyewaById(id));
    }

    // Update nama pengguna berdasarkan ID
    public void updateUserName(int userId, String newName) {
        executor.execute(() -> db.penyewaDao().updateNamaById(userId, newName));
    }

    // Update email pengguna berdasarkan ID
    public void updateUserEmail(int userId, String newEmail) {
        executor.execute(() -> db.penyewaDao().updateEmailById(userId, newEmail));
    }

    // Update nomor HP pengguna berdasarkan ID
    public LiveData<String> updateUserPhone(int userId, String newPhone) {
        MutableLiveData<String> result = new MutableLiveData<>();

        if (!newPhone.matches("\\d+")) {
            // Validasi gagal jika tidak hanya berisi angka
            result.postValue("Phone Number can only contain numbers");
            return result;
        }

        executor.execute(() -> {
            try {
                db.penyewaDao().updatePhoneById(userId, newPhone);
                result.postValue("success"); // Pembaruan berhasil
            } catch (Exception e) {
                result.postValue("failed"); // Kesalahan dalam proses pembaruan
            }
        });

        return result;
    }


}



