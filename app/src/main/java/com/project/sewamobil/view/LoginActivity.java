package com.project.sewamobil.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.project.sewamobil.R;
import com.project.sewamobil.model.Penyewa;
import com.project.sewamobil.viewmodel.PenyewaViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private PenyewaViewModel penyewaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connect UI elements to code
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize ViewModel
        penyewaViewModel = new ViewModelProvider(this).get(PenyewaViewModel.class);

        // Observe LiveData
        penyewaViewModel.getPenyewaLiveData().observe(this, penyewa -> {
            if (penyewa != null) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Save login status and user details to SharedPreferences
                getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .putInt("userId", penyewa.getId())
                        .putString("userName", penyewa.getNama())
                        .putString("userEmail", penyewa.getEmail())
                        .putString("userPhone", penyewa.getNoHp())
                        .putString("userAddress", penyewa.getAlamat())
                        .apply();

                // Redirect to MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the login button
        btnLogin.setOnClickListener(v -> loginUser());

        // Set click listener for the registration page redirect
        TextView tvDontHaveAccount = findViewById(R.id.tvDontHaveAccount);
        tvDontHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        penyewaViewModel.loginUser(email, password);
    }
}
