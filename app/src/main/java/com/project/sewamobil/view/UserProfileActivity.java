package com.project.sewamobil.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.project.sewamobil.R;
import com.project.sewamobil.model.Penyewa;
import com.project.sewamobil.viewmodel.PenyewaViewModel;

public class UserProfileActivity extends AppCompatActivity {

    private TextView displayName, displayEmail, displayPhone, displayAddress;
    private EditText editName, editEmail, editPhone, editAddress;
    private ImageView editNameButton, saveNameButton, editEmailButton, saveEmailButton, editPhoneButton, savePhoneButton, editAddressButton, saveAddressButton;
    private Button logoutButton;
    private PenyewaViewModel penyewaViewModel;
    private int userId;  // ID pengguna yang sedang login, diambil dari SharedPreferences atau database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Inisialisasi komponen UI
        displayName = findViewById(R.id.display_name);
        editName = findViewById(R.id.edit_name);
        editNameButton = findViewById(R.id.edit_name_button);
        saveNameButton = findViewById(R.id.save_name_button);

        displayEmail = findViewById(R.id.display_email);
        editEmail = findViewById(R.id.edit_email);
        editEmailButton = findViewById(R.id.edit_email_button);
        saveEmailButton = findViewById(R.id.save_email_button);

        displayPhone = findViewById(R.id.display_phone);
        editPhone = findViewById(R.id.edit_phone);
        editPhoneButton = findViewById(R.id.edit_phone_button);
        savePhoneButton = findViewById(R.id.save_phone_button);

        displayAddress = findViewById(R.id.display_address);
        editAddress = findViewById(R.id.edit_address);
        editAddressButton = findViewById(R.id.edit_address_button);
        saveAddressButton = findViewById(R.id.save_address_button);
        ImageView backButton = findViewById(R.id.back_button);





        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional, jika Anda ingin menutup activity ini setelah berpindah ke MainActivity
            }
        });
        // Ambil ID pengguna dari SharedPreferences atau database
        userId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getInt("userId", -1);

        // Inisialisasi ViewModel
        penyewaViewModel = new ViewModelProvider(this).get(PenyewaViewModel.class);

        // Ambil data user dari database menggunakan ViewModel
        penyewaViewModel.getPenyewaById(userId).observe(this, penyewa -> {
            if (penyewa != null) {
                displayName.setText(penyewa.getNama());
                displayEmail.setText(penyewa.getEmail());
                displayPhone.setText(penyewa.getNoHp());
                displayAddress.setText(penyewa.getAlamat());
            }
        });



        // Edit Nama
        setupEditSaveFunctionality(editName, displayName, editNameButton, saveNameButton, "name");

        // Edit Email
        setupEditSaveFunctionality(editEmail, displayEmail, editEmailButton, saveEmailButton, "email");

        // Edit Phone
        setupEditSaveFunctionality(editPhone, displayPhone, editPhoneButton, savePhoneButton, "phone");

        // Edit Address
        setupEditSaveFunctionality(editAddress, displayAddress, editAddressButton, saveAddressButton, "address");
    }

    private void setupEditSaveFunctionality(EditText editField, TextView displayField, ImageView editButton, ImageView saveButton, String field) {
        editButton.setOnClickListener(v -> {
            editField.setVisibility(View.VISIBLE);
            displayField.setVisibility(View.GONE);
            editField.setText(displayField.getText());
            saveButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
        });

        saveButton.setOnClickListener(v -> {
            String newValue = editField.getText().toString();

            // Cek validasi sebelum mengubah UI
            boolean isValid = validateAndSaveData(userId, newValue, field);
            if (isValid) { // Hanya ubah UI jika validasi berhasil
                displayField.setText(newValue);
                displayField.setVisibility(View.VISIBLE);
                editField.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean validateAndSaveData(int userId, String newValue, String field) {
        switch (field) {
            case "name":
                if (!newValue.matches("[a-zA-Z ]+")) { // Validasi hanya huruf dan spasi
                    Toast.makeText(this, "Name can only contain letters and spaces", Toast.LENGTH_SHORT).show();
                    return false;
                }
                penyewaViewModel.updateUserName(userId, newValue);
                break;

            case "email":
                if (!newValue.contains("@")) { // Validasi email harus mengandung '@'
                    Toast.makeText(this, "Email must contain @", Toast.LENGTH_SHORT).show();
                    return false;
                }
                penyewaViewModel.updateUserEmail(userId, newValue);
                break;

            case "phone":
                if (!newValue.matches("\\d{12}")) { // Validasi nomor telepon harus 12 digit
                    Toast.makeText(this, "Phone number must be exactly 12 digits", Toast.LENGTH_SHORT).show();
                    return false;
                }
                penyewaViewModel.updateUserPhone(userId, newValue);
                break;

            case "address":
                penyewaViewModel.updateUserAddress(userId, newValue);
                break;
        }

        // Jika semua validasi berhasil
        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
        return true;
    }



    private void saveData(int userId, String newValue, String field) {
        switch (field) {
            case "name":
                penyewaViewModel.updateUserName(userId, newValue);
                break;
            case "email":
                penyewaViewModel.updateUserEmail(userId, newValue);
                break;
            case "phone":
                if (!newValue.matches("\\d+")) { // Validasi hanya angka
                    Toast.makeText(this, "Phone number can only contain numbers", Toast.LENGTH_SHORT).show();
                    return; // Hentikan proses jika validasi gagal
                }
                penyewaViewModel.updateUserPhone(userId, newValue);
                break;

            case "address":
                penyewaViewModel.updateUserAddress(userId, newValue);
                break;
        }
        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
    }





}
