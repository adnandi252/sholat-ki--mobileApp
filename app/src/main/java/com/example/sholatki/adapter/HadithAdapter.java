package com.example.sholatki.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sholatki.DatabaseHelper;
import com.example.sholatki.HadithActivity;
import com.example.sholatki.R;
import com.example.sholatki.models.Hadith;

import java.util.List;


public class HadithAdapter extends RecyclerView.Adapter<HadithAdapter.HadithViewHolder>{
    private Context context;
    private List<Hadith> hadithList;
    DatabaseHelper databaseHelper;

    public HadithAdapter(Context context, List<Hadith> hadithList) {
        this.context = context;
        this.hadithList = hadithList;
    }

    @NonNull
    @Override
    public HadithViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hadith, parent, false);
        return new HadithViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HadithViewHolder holder, int position) {
        Hadith hadith = hadithList.get(position);

        holder.tv_judul_bookmark.setText("Imam " + hadith.getPerawi() + "\nNomor: " + hadith.getNumber());
        holder.tv_hadith_bookmark.setText(hadith.getArabicHadith() + "\n\n" + hadith.getIndonesianHadith());

        databaseHelper = new DatabaseHelper(context);
        holder.btn_delete_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteHadith(String.valueOf(hadith.getId()));
                Intent intent = new Intent(context, HadithActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hadithList.size();
    }

    public class HadithViewHolder extends RecyclerView.ViewHolder {
        TextView tv_judul_bookmark, tv_hadith_bookmark;
        ImageButton btn_delete_bookmark;

        public HadithViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judul_bookmark = itemView.findViewById(R.id.tv_judul_bookmark);
            tv_hadith_bookmark = itemView.findViewById(R.id.tv_hadith_bookmark);
            btn_delete_bookmark = itemView.findViewById(R.id.btn_delete_bookmark);
        }
    }
}
