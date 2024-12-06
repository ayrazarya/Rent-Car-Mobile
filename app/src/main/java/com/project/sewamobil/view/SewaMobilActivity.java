package com.project.sewamobil.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.project.sewamobil.R;
import com.project.sewamobil.viewmodel.MobilViewModel;
import com.project.sewamobil.viewmodel.PenyewaViewModel;
import com.project.sewamobil.viewmodel.SewaViewModel;

import java.util.Calendar;
import java.util.Locale;

public class SewaMobilActivity extends AppCompatActivity {

    private MobilViewModel mobilViewModel;
    private ImageView backButton, carImage; // sesuai dengan id di XML
    private TextView title, renterInfoTitle, carInfoTitle, tvName, tvAddress, tvPhoneNumber, carPrice, tvRentalDuration, tvTotalPrice, carModel, promoText;
    private Button btnFinish;
    private PenyewaViewModel penyewaViewModel;
    private int userId;  // ID pengguna yang sedang login, diambil dari SharedPreferences atau database
    private Calendar startDate, endDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewa); // sesuai nama file XML Anda

        mobilViewModel = new ViewModelProvider(this).get(MobilViewModel.class);
        penyewaViewModel = new ViewModelProvider(this).get(PenyewaViewModel.class);

        // Inisialisasi view berdasarkan ID di XML
        backButton = findViewById(R.id.back_button);
        title = findViewById(R.id.title);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        carImage = findViewById(R.id.car_image);
        carModel = findViewById(R.id.tvCarModel);
        carPrice = findViewById(R.id.tvCarPrice);
        tvRentalDuration = findViewById(R.id.tvRentalDuration);
        promoText = findViewById(R.id.promo_text);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnFinish = findViewById(R.id.btnFinish);

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // Retrieve the logged-in user's ID from SharedPreferences
        userId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getInt("userId", -1);

        if (userId != -1) {
            // Fetch user details from database
            penyewaViewModel.getPenyewaById(userId).observe(this, penyewa -> {
                if (penyewa != null) {
                    // Populate user details in UI
                    tvName.setText(penyewa.getNama());
                    tvAddress.setText(penyewa.getAlamat());
                    tvPhoneNumber.setText(penyewa.getNoHp());
                }
            });
        }

        // Ambil data dari Intent
        int mobilId = getIntent().getIntExtra("id", -1);
        String model = getIntent().getStringExtra("model");
        double price = getIntent().getDoubleExtra("price", 0);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        if (mobilId != -1) {
            mobilViewModel.getMobilById(mobilId).observe(this, mobil -> {
                if (mobil != null) {
                    updateUI(mobil.getModel(), mobil.getPrice(), mobil.getImage());
                } else {
                    Toast.makeText(SewaMobilActivity.this, "Mobil tidak ditemukan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            updateUI(model, price, imageUrl);
        }

        backButton.setOnClickListener(v -> onBackPressed());
        tvRentalDuration.setOnClickListener(v -> showDatePickerDialog());
        btnFinish.setOnClickListener(v -> {
            // Assuming you already have rentalDuration and totalPrice calculated
            int rentalDuration = Integer.parseInt(tvRentalDuration.getText().toString().replace("Rental Duration: ", "").replace(" Days", "").trim());
            double totalPrice = Double.parseDouble(tvTotalPrice.getText().toString().replace("Total Price: $", "").trim());

            finishRental(rentalDuration, totalPrice); // Pass rental duration and total price
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog startDatePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    startDate.set(year, month, dayOfMonth);
                    showEndDatePickerDialog();
                },
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH)
        );
        startDatePicker.setTitle("Select Start Date");
        startDatePicker.show();
    }

    private void showEndDatePickerDialog() {
        DatePickerDialog endDatePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    endDate.set(year, month, dayOfMonth);
                    calculateRentalDuration();
                },
                endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH)
        );
        endDatePicker.setTitle("Select End Date");
        endDatePicker.show();
    }

    private void calculateRentalDuration() {
        long durationInMillis = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        int rentalDuration = (int) (durationInMillis / (1000 * 60 * 60 * 24));

        if (rentalDuration > 0) {
            tvRentalDuration.setText("Rental Duration: " + rentalDuration + " Days");
            updateTotalPrice(rentalDuration);
        } else {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalPrice(int rentalDuration) {
        try {
            double pricePerDay = Double.parseDouble(carPrice.getText().toString()
                    .replace("Price: $", "")
                    .replace("/day", "")
                    .trim());

            // Total price calculation
            double totalPrice = rentalDuration * pricePerDay;

            // Calculate discount percentage
            double discount = 0;

            // Discount for multiples of 3 days
            if (rentalDuration >= 3) {
                int multiplesOfThree = rentalDuration / 3;
                discount += multiplesOfThree * 0.10; // 10% for every 3 days
            }

            // Discount for multiples of 5 days
            if (rentalDuration >= 5) {
                int multiplesOfFive = rentalDuration / 5;
                discount += multiplesOfFive * 0.25; // 25% for every 5 days
            }

            // Ensure discount doesn't exceed 50%
            discount = Math.min(discount, 0.50);

            // Calculate the discounted price
            double discountedPrice = totalPrice * (1 - discount);

            // Update the UI with the discounted price and promo text
            tvTotalPrice.setText(String.format(Locale.getDefault(), "Total Price: $%.2f", discountedPrice));
            promoText.setText(String.format(Locale.getDefault(), "Promo: %.0f%% off", discount * 100));

            // Save the promo percentage (as an integer) for saving to the database
            int promoPercentage = (int) (discount * 100);  // Convert to percentage (e.g., 10% = 10)

            // Save rental data along with the promo percentage
            btnFinish.setOnClickListener(v -> finishRental(promoPercentage, discountedPrice));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error calculating price. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private void finishRental(int promoPercentage, double totalPrice) {
        String name = tvName.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();
        String phone = tvPhoneNumber.getText().toString().trim();
        String durationText = tvRentalDuration.getText().toString().replace("Rental Duration: ", "").replace(" Days", "").trim();
        String totalText = tvTotalPrice.getText().toString()
                .replace("Total Price: $", "")
                .trim()
                .replace(",", ".");
        if (!totalText.isEmpty()) {
            double total = Double.parseDouble(totalText);
        }

        String carModel = this.carModel.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || durationText.isEmpty() || totalText.isEmpty() || carModel.isEmpty()) {
            Toast.makeText(this, "Please complete all fields before finishing the rental.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int rentalDuration = Integer.parseInt(durationText);
            double total = Double.parseDouble(totalText);

            // Insert rental data into the database via ViewModel
            SewaViewModel sewaViewModel = new ViewModelProvider(this).get(SewaViewModel.class);
            sewaViewModel.insert(carModel, name, address, phone, rentalDuration, promoPercentage, total);

            // Observe the operation status
            sewaViewModel.getOperationStatus().observe(this, status -> {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
                if (status.equals("Sewa berhasil disimpan")) {
                    // After successful rental, navigate to PenyewaActivity
                    Intent intent = new Intent(SewaMobilActivity.this, PenyewaActivity.class);
                    intent.putExtra("USER_ID", userId); // Pass the userId
                    startActivity(intent);
                    finish(); // Close the current activity

                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please check your data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String model, double price, String imageUrl) {
        title.setText("Rental Details for " + model);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(carImage);
        } else {
            carImage.setImageResource(R.drawable.ertiga); // Ganti sesuai kebutuhan
        }

        carModel.setText(model);
        carPrice.setText("Price: $" + price + "/day");
    }
}
