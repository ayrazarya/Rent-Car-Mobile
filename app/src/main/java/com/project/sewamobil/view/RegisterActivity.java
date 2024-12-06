package com.project.sewamobil.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.project.sewamobil.R;
import com.project.sewamobil.db.AppDatabase;
import com.project.sewamobil.model.Penyewa;
import androidx.lifecycle.ViewModelProvider;
import com.project.sewamobil.viewmodel.PenyewaViewModel;


public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private PenyewaViewModel penyewaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Menghubungkan komponen UI dengan kode Java
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Inisialisasi ViewModel
        penyewaViewModel = new ViewModelProvider(this).get(PenyewaViewModel.class);

        // Menetapkan aksi untuk tombol Register
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        // Mendapatkan input dari pengguna
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validasi form
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Memastikan password dan confirm password cocok
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Panggil ViewModel untuk melakukan registrasi
        penyewaViewModel.registerUser(email, password).observe(this, result -> {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            if ("Registration successful!".equals(result)) {
                finish();  // Menutup activity setelah berhasil registrasi
            }
        });
    }
}
