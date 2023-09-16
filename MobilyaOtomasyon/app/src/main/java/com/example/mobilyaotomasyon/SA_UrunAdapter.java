package com.example.mobilyaotomasyon;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SA_UrunAdapter extends RecyclerView.Adapter<SA_UrunAdapter.ViewHolder> {

    private ArrayList<SA_Urunitem> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView SA_isim;
        private final TextView SA_fiyat;
        private final TextView SA_aciklama;
        private final TextView SA_kalite;
        private final TextView SA_renk;
        private final TextView SA_sehir;
        private final ImageView SA_foto;

        public ViewHolder(View view) {
            super(view);
            SA_isim = (TextView) view.findViewById(R.id.SA_txt_ana_item_isim);
            SA_fiyat = (TextView) view.findViewById(R.id.SA_txt_ana_item_fiyat);
            SA_aciklama = (TextView) view.findViewById(R.id.SA_txt_ana_item_aciklama);
            SA_kalite = (TextView) view.findViewById(R.id.SA_txt_ana_item_kalite);
            SA_renk = (TextView) view.findViewById(R.id.SA_txt_ana_item_renk);
            SA_sehir = (TextView) view.findViewById(R.id.SA_txt_ana_item_sehir);
            SA_foto = view.findViewById(R.id.SA_iv_ana_item_foto);
        }
    }

    public SA_UrunAdapter(ArrayList<SA_Urunitem> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sa_analiste_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.SA_isim.setText(localDataSet.get(position).SA_isim);
        viewHolder.SA_fiyat.setText(localDataSet.get(position).SA_fiyat);
        viewHolder.SA_aciklama.setText(localDataSet.get(position).SA_aciklama);
        viewHolder.SA_kalite.setText(localDataSet.get(position).SA_kalite);
        viewHolder.SA_renk.setText(localDataSet.get(position).SA_renk);
        viewHolder.SA_sehir.setText(localDataSet.get(position).SA_sehir);
        String url = localDataSet.get(position).SA_url;
        Picasso.get().load(url).into(viewHolder.SA_foto);

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}