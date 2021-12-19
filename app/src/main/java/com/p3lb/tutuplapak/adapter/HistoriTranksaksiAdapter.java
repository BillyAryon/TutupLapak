package com.p3lb.tutuplapak.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p3lb.tutuplapak.R;
import com.p3lb.tutuplapak.model.History;

import java.util.List;
import java.util.Locale;

public class HistoriTranksaksiAdapter extends RecyclerView.Adapter<HistoriTranksaksiAdapter.MyViewHolder> {
    List<History> historyList;
    public static String idkuu = "";
    public HistoriTranksaksiAdapter(List<History> histories) {
        historyList = histories;
    }


    @Override
    public HistoriTranksaksiAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_data_history_transaksi, parent, false);
        HistoriTranksaksiAdapter.MyViewHolder mViewHolder = new HistoriTranksaksiAdapter.MyViewHolder(mView);
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder (HistoriTranksaksiAdapter.MyViewHolder holder, final int position){
        holder.idtransaksi.setText(historyList.get(position).getIdTransaksi());
        holder.namauser.setText(historyList.get(position).getNamaUser());
        holder.alamatpembeli.setText(historyList.get(position).getNamaPembeli());
        holder.tanggal.setText(historyList.get(position).getTanggal());
        int number = Integer.parseInt(historyList.get(position).getTotalBayar());
        String str = String.format(Locale.US, "%,d", number).replace(',', '.');
        holder.totalbayar.setText("Rp "+str);
        boolean isExpanded = historyList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        Log.d("sizediskon ",""+historyList.size());
        return historyList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idtransaksi, totalbayar, namauser, alamatpembeli, tanggal;
        RelativeLayout expandableLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            idtransaksi = (TextView) itemView.findViewById(R.id.idtransaksi);
            alamatpembeli = (TextView) itemView.findViewById(R.id.namapembeli);
            totalbayar = (TextView) itemView.findViewById(R.id.totalbayar);
            namauser = (TextView) itemView.findViewById(R.id.namauser);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            namauser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    History history = historyList.get(getAdapterPosition());
                    history.setExpanded(!history.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            totalbayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    History history = historyList.get(getAdapterPosition());
                    history.setExpanded(!history.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
