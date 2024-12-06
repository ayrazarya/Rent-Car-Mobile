package com.project.sewamobil.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.sewamobil.R;
import com.project.sewamobil.model.Mobil;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Mobil> mobilList;

    // Constructor untuk menginisialisasi daftar mobil
    public CarAdapter(List<Mobil> mobilList) {
        this.mobilList = mobilList;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Mobil mobil = mobilList.get(position);

        // Tampilkan data model dan harga
        holder.carModel.setText(mobil.getModel());
        holder.carPrice.setText("$ " + mobil.getPrice() + "/Day");

        // Muat gambar menggunakan Glide
        Glide.with(holder.itemView.getContext())
                .load(mobil.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.carImage);

        // Tambahkan listener klik untuk mengirim data ke DetailMobilActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailMobilActivity.class);

            // Kirim ID mobil atau semua data lainnya
            intent.putExtra("id", mobil.getId()); // Kirim ID mobil
            intent.putExtra("model", mobil.getModel());
            intent.putExtra("price", mobil.getPrice());
            intent.putExtra("image", mobil.getImage());
            intent.putExtra("fuelType", mobil.getFuelType());
            intent.putExtra("transmission", mobil.getTransmission());

            holder.itemView.getContext().startActivity(intent);
        });

        // Menambahkan aksi klik pada tombol "Rent Now"
        holder.rentNowButton.setOnClickListener(v -> {
            // Aksi yang dilakukan saat tombol diklik


            // Logika tambahan seperti membuka aktivitas lain bisa ditambahkan di sini
            Intent rentIntent = new Intent(holder.itemView.getContext(), SewaMobilActivity.class);
            rentIntent.putExtra("id", mobil.getId()); // Pass mobil ID
            rentIntent.putExtra("model", mobil.getModel()); // Pass model name
            rentIntent.putExtra("price", mobil.getPrice()); // Pass price
            rentIntent.putExtra("imageUrl", mobil.getImage()); // Corrected key name
            rentIntent.putExtra("fuelType", mobil.getFuelType()); // Pass fuel type
            rentIntent.putExtra("transmission", mobil.getTransmission()); // Pass transmission type
            holder.itemView.getContext().startActivity(rentIntent); // Start Rent activity
        });
    }

    @Override
    public int getItemCount() {
        return mobilList != null ? mobilList.size() : 0;
    }

    // ViewHolder untuk item mobil
    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carModel, carPrice;
        ImageView carImage;
        Button rentNowButton; // Menambahkan Button untuk Rent Now

        public CarViewHolder(View itemView) {
            super(itemView);

            carModel = itemView.findViewById(R.id.car_model);
            carPrice = itemView.findViewById(R.id.car_price);
            carImage = itemView.findViewById(R.id.car_image);
            rentNowButton = itemView.findViewById(R.id.btn_rent_now); // Inisialisasi tombol Rent Now
        }
    }

    // Method untuk memperbarui daftar data mobil
    public void setMobilList(List<Mobil> mobilList) {
        this.mobilList = mobilList;
        notifyDataSetChanged(); // Memberi tahu RecyclerView untuk memperbarui tampilan
    }
}
