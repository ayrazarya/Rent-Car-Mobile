package com.project.sewamobil.view;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.project.sewamobil.R;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.viewmodel.MobilViewModel;
import com.project.sewamobil.viewmodel.PenyewaViewModel;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView locationTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private PenyewaViewModel penyewaViewModel;
    private ImageView profileIcon;
    private RecyclerView carList;
    private MobilViewModel mobilViewModel;
    private CarAdapter carAdapter;
    private TextView favoritesTextView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private LinearLayout historyLayout;
    private LinearLayout logoutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        penyewaViewModel = new ViewModelProvider(this).get(PenyewaViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        carList = findViewById(R.id.car_list);
        locationTextView = findViewById(R.id.location);
        favoritesTextView = findViewById(R.id.favorites);
        EditText searchBar = findViewById(R.id.search_bar);
        ImageView searchButton = findViewById(R.id.search_button);

        favoritesTextView.setText("Let's Find Your Favorite Car!");

        mobilViewModel = new ViewModelProvider(this).get(MobilViewModel.class);
        carList.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(null);
        carList.setAdapter(carAdapter);

        mobilViewModel.getAllMobil().observe(this, new Observer<List<Mobil>>() {
            @Override
            public void onChanged(List<Mobil> mobilList) {
                carAdapter.setMobilList(mobilList);
            }
        });

        // Tambahkan TextWatcher untuk searchBar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak diperlukan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    mobilViewModel.searchMobil(query).observe(MainActivity.this, new Observer<List<Mobil>>() {
                        @Override
                        public void onChanged(List<Mobil> mobilList) {
                            carAdapter.setMobilList(mobilList);
                        }
                    });
                } else {
                    mobilViewModel.getAllMobil().observe(MainActivity.this, new Observer<List<Mobil>>() {
                        @Override
                        public void onChanged(List<Mobil> mobilList) {
                            carAdapter.setMobilList(mobilList);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak diperlukan
            }
        });

        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                mobilViewModel.searchMobil(query).observe(this, new Observer<List<Mobil>>() {
                    @Override
                    public void onChanged(List<Mobil> mobilList) {
                        carAdapter.setMobilList(mobilList);
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Masukkan kata kunci pencarian", Toast.LENGTH_SHORT).show();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);

        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        historyLayout = findViewById(R.id.history_layout);
        logoutLayout = findViewById(R.id.logout_layout);

        historyLayout.setOnClickListener(v -> {
            int userId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getInt("userId", -1);
            if (userId == -1) {
                Toast.makeText(MainActivity.this, "User ID tidak valid!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, PenyewaActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(v -> {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(v -> {
            // Cek status login dari SharedPreferences
            boolean isLoggedIn = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // Jika sudah login, arahkan ke UserProfileActivity
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            } else {
                // Jika belum login, arahkan ke LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);

                                String country = address.getCountryName();
                                String city = address.getLocality();

                                String locationText = (city != null ? city : country);
                                if (locationText == null) {
                                    locationText = "Location not found.";
                                }
                                locationTextView.setText(locationText);

                                getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                                        .edit()
                                        .putString("userAddress", locationText)
                                        .apply();

                                int userId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getInt("userId", -1);

                                if (userId != -1) {
                                    penyewaViewModel.updateUserAddress(userId, locationText);
                                }

                            } else {
                                locationTextView.setText("Location not found.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            locationTextView.setText("Geocoder failed.");
                        }
                    } else {
                        locationTextView.setText("Location not available.");
                    }
                });
    }

    private final ActivityResultCallback<Boolean> permissionResultCallback = result -> {
        if (result) {
            getLocation();
        } else {
            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    };

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), permissionResultCallback);
}
