package com.project.sewamobil.view;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sewamobil.R;
import com.project.sewamobil.model.Mobil;

import java.util.List;

public class PenyewaAdapter extends RecyclerView.Adapter<PenyewaAdapter.PenyewaViewHolder> {

    private List<Mobil> mobilList;
    private OnItemSwipeListener swipeListener;

    public PenyewaAdapter(List<Mobil> mobilList, OnItemSwipeListener swipeListener) {
        this.mobilList = mobilList;
        this.swipeListener = swipeListener;
    }

    @NonNull
    @Override
    public PenyewaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_penyewa, parent, false);
        return new PenyewaViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PenyewaViewHolder holder, int position) {
        if (mobilList != null) {
            Mobil mobil = mobilList.get(position);
            holder.mobilName.setText(mobil.getModel());
            holder.mobilDetails.setText("Price: " + mobil.getPrice() + " | Year: " + mobil.getYear());

            final GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    // Jika jarak horizontal > 200 dan kecepatan > 2000, dianggap swipe kiri
                    if (e1.getX() - e2.getX() > 200 && Math.abs(velocityX) > 2000) {
                        swipeListener.onItemSwipe(mobil.getId(), holder.getAdapterPosition());
                        return true;
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });

            holder.itemView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        }
    }

    public void updateList(List<Mobil> newMobilList) {
        this.mobilList = newMobilList;
        notifyDataSetChanged();
    }





    @Override
    public int getItemCount() {
        return mobilList == null ? 0 : mobilList.size();
    }

    static class PenyewaViewHolder extends RecyclerView.ViewHolder {
        TextView mobilName, mobilDetails;

        public PenyewaViewHolder(@NonNull View itemView) {
            super(itemView);
            mobilName = itemView.findViewById(R.id.mobil_name);
            mobilDetails = itemView.findViewById(R.id.mobil_details);
        }
    }

    public interface OnItemSwipeListener {
        void onItemSwipe(int mobilId, int position);
    }
}
