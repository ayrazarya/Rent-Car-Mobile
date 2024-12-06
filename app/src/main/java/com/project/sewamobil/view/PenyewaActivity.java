package com.project.sewamobil.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sewamobil.R;
import com.project.sewamobil.model.Mobil;
import com.project.sewamobil.model.Penyewa;
import com.project.sewamobil.viewmodel.SewaViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.util.List;

public class PenyewaActivity extends AppCompatActivity implements PenyewaAdapter.OnItemSwipeListener {

    private SewaViewModel sewaViewModel;
    private PenyewaAdapter adapter;
    private int userId;  // Deklarasikan userId
    private List<Mobil> mobilList; // Declared at the class level



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyewa);
        ImageView backButton = findViewById(R.id.back_button);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sewaViewModel = new ViewModelProvider(this).get(SewaViewModel.class);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID tidak valid!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenyewaActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional, jika Anda ingin menutup activity ini setelah berpindah ke MainActivity
            }
        });

        sewaViewModel.getMobilByUserId(userId).observe(this, mobilList -> {
            if (mobilList != null && !mobilList.isEmpty()) {
                this.mobilList = mobilList;
                adapter = new PenyewaAdapter(mobilList, this);
                recyclerView.setAdapter(adapter);

                // Tambahkan ItemTouchHelper dengan dialog konfirmasi
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false; // Tidak ada drag-and-drop
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Mobil mobil = mobilList.get(position);

                        // Tampilkan dialog konfirmasi
                        new AlertDialog.Builder(PenyewaActivity.this)
                                .setTitle("Delete Rental")
                                .setMessage("Are you sure you want to delete this rental?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    // Validasi dan hapus item jika pengguna menekan "Yes"
                                    sewaViewModel.getSewaById(mobil.getId()).observe(PenyewaActivity.this, sewa -> {
                                        if (sewa != null && sewa.getPenyewaId() == userId) {
                                            sewaViewModel.deleteSewaById(sewa.getId());
                                            mobilList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            Toast.makeText(PenyewaActivity.this, "Rental deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PenyewaActivity.this, "This rental does not belong to the current user", Toast.LENGTH_SHORT).show();
                                            adapter.notifyItemChanged(position); // Kembalikan item ke posisi awal
                                        }
                                    });
                                })
                                .setNegativeButton("No", (dialog, which) -> {
                                    // Kembalikan item ke posisi awal jika pengguna menekan "No"
                                    adapter.notifyItemChanged(position);
                                })
                                .setCancelable(false) // Tidak bisa ditutup tanpa memilih
                                .show();
                    }
                });
                itemTouchHelper.attachToRecyclerView(recyclerView);
            } else {
                Toast.makeText(this, "No rental history found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSwipe(int sewaId, int position) {
        // Memeriksa apakah Sewa dengan sewaId terkait dengan userId yang aktif
        sewaViewModel.getSewaById(sewaId).observe(this, sewa -> {
            if (sewa != null && sewa.getPenyewaId() == userId) {
                // Jika penyewaId sesuai dengan userId yang aktif, hapus Sewa
                sewaViewModel.deleteSewaById(sewaId);
                Toast.makeText(this, "Rental deleted", Toast.LENGTH_SHORT).show();

                // Menghapus item dari mobilList
                mobilList.remove(position); // Menghapus item dari list

                // Memperbarui list di adapter dan memberi tahu adapter bahwa item telah dihapus
                adapter.updateList(mobilList); // Memanggil metode updateList di adapter
            } else {
                Toast.makeText(this, "This rental does not belong to the current user", Toast.LENGTH_SHORT).show();
            }
        });
    }





}
