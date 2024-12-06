package com.project.sewamobil.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.project.sewamobil.R;
import com.project.sewamobil.api.ApiClient;
import com.project.sewamobil.api.ApiService;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.viewmodel.MobilViewModel;

import java.util.ArrayList;
import java.util.List;

public class DaftarMobilActivity extends AppCompatActivity {

    private ListView listView;
    private MobilViewModel mobilViewModel;
    private ArrayAdapter<Mobil> adapter;
    private TextView tvEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobil);

        listView = findViewById(R.id.listView1);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        setupToolbar();

        mobilViewModel = new ViewModelProvider(this).get(MobilViewModel.class);
        ApiService apiService = ApiClient.getApiService();

        // Fetch data from the API
        mobilViewModel.syncDataFromApi(apiService);

        // Set up the ArrayAdapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Observe the mobil list from the ViewModel
        mobilViewModel.getAllMobil().observe(this, mobilList -> {
            if (mobilList != null && !mobilList.isEmpty()) {
                adapter.clear();
                adapter.addAll(mobilList);
                tvEmptyList.setVisibility(View.GONE); // Hide empty list message
                listView.setVisibility(View.VISIBLE);
            } else {
                tvEmptyList.setVisibility(View.VISIBLE); // Show empty list message
                listView.setVisibility(View.GONE);
            }
        });

        // Set up the click listener for list items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Mobil selectedMobil = adapter.getItem(position);

            // Passing selected mobil details to the DetailMobilActivity
            Intent intent = new Intent(DaftarMobilActivity.this, DetailMobilActivity.class);
            intent.putExtra("id", selectedMobil.getId());
            intent.putExtra("merk", selectedMobil.getMake());
            intent.putExtra("model", selectedMobil.getModel());
            intent.putExtra("year", selectedMobil.getYear());
            intent.putExtra("color", selectedMobil.getColor());
            intent.putExtra("price", selectedMobil.getPrice());
            intent.putExtra("fuelType", selectedMobil.getFuelType());
            intent.putExtra("transmission", selectedMobil.getTransmission());
            intent.putExtra("engine", selectedMobil.getEngine());
            intent.putExtra("horsepower", selectedMobil.getHorsepower());
            intent.putStringArrayListExtra("features", new ArrayList<>(selectedMobil.getFeatures()));
            intent.putExtra("owners", selectedMobil.getOwners());
            intent.putExtra("image", selectedMobil.getImage());
            startActivity(intent);
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbInfoMbl);
        toolbar.setTitle("Informasi Daftar Mobil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
