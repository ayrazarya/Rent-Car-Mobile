package com.project.sewamobil.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.project.sewamobil.R;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.viewmodel.MobilViewModel;

import java.util.List;

public class DetailMobilActivity extends AppCompatActivity {

    private MobilViewModel mobilViewModel;

    // Declare UI elements
    private TextView modelTextView, priceTextView, fuelTypeTextView, transmissionTextView, feature1TextView, feature2TextView, feature3TextView;
    private ImageView imageView, backButton;
    private Button rentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mobil);

        // Initialize ViewModel
        mobilViewModel = new ViewModelProvider(this).get(MobilViewModel.class);

        // Initialize UI components
        modelTextView = findViewById(R.id.car_model);
        priceTextView = findViewById(R.id.car_price);
        fuelTypeTextView = findViewById(R.id.car_fuel_type);
        transmissionTextView = findViewById(R.id.car_transmission);
        feature1TextView = findViewById(R.id.feature_1);
        feature2TextView = findViewById(R.id.feature_2);
        feature3TextView = findViewById(R.id.feature_3);
        imageView = findViewById(R.id.car_image);
        rentButton = findViewById(R.id.rent_button);
        backButton = findViewById(R.id.back_button); // Your custom back button

        // Handle the custom back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();  // Navigate back to previous activity
            }
        });

        // Retrieve data from Intent
        int mobilId = getIntent().getIntExtra("id", -1);
        String model = getIntent().getStringExtra("model");
        double price = getIntent().getDoubleExtra("price", 0);
        String imageUrl = getIntent().getStringExtra("image");
        String fuelType = getIntent().getStringExtra("fuelType");
        String transmission = getIntent().getStringExtra("transmission");

        // Update UI with Intent data if mobilId is -1
        if (mobilId != -1) {
            mobilViewModel.getMobilById(mobilId).observe(this, new Observer<Mobil>() {
                @Override
                public void onChanged(Mobil mobil) {
                    if (mobil != null) {
                        updateUI(mobil.getModel(), mobil.getPrice(), mobil.getImage(),
                                mobil.getFuelType(), mobil.getTransmission(), mobil.getFeatures());
                    } else {
                        Toast.makeText(DetailMobilActivity.this, "Mobil tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            updateUI(model, price, imageUrl, fuelType, transmission, null); // If no ID is provided
        }

        // Rent button functionality
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass data to SewaMobilActivity
                Intent intent = new Intent(DetailMobilActivity.this, SewaMobilActivity.class);
                intent.putExtra("id", mobilId); // Pass mobil ID
                intent.putExtra("model", model); // Pass model name
                intent.putExtra("price", price); // Pass price
                intent.putExtra("imageUrl", imageUrl); // Corrected key name
                intent.putExtra("fuelType", fuelType); // Pass fuel type
                intent.putExtra("transmission", transmission); // Pass transmission type
                startActivity(intent);
            }
        });
    }


        private void updateUI(String model, double price, String imageUrl,
                          String fuelType, String transmission, List<String> features) {
        // Set model, price, fuel type, transmission, and features to respective UI components
        modelTextView.setText(model);
        priceTextView.setText(price + "$/Day");
        fuelTypeTextView.setText("Fuel Type: " + fuelType);
        transmissionTextView.setText("Transmission: " + transmission);

        // Handle features
        if (features != null && !features.isEmpty()) {
            if (features.size() > 0) feature1TextView.setText("1.  " + features.get(0));
            if (features.size() > 1) feature2TextView.setText("2.  " + features.get(1));
            if (features.size() > 2) feature3TextView.setText("3. " + features.get(2));
        } else {
            feature1TextView.setText("No features available");
            feature2TextView.setVisibility(View.GONE);
            feature3TextView.setVisibility(View.GONE);
        }

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(imageView);
    }
}
