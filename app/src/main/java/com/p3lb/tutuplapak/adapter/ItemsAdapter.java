package com.p3lb.tutuplapak.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.p3lb.tutuplapak.ActivitySelectMenu;
import com.p3lb.tutuplapak.Config;
import com.p3lb.tutuplapak.R;
import com.p3lb.tutuplapak.model.Products;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    List<Products> mmenuslist;

    public ItemsAdapter(List<Products> menuList) {
        mmenuslist = menuList;
    }


    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_menu, parent, false);
        ItemsAdapter.MyViewHolder mViewHolder = new ItemsAdapter.MyViewHolder(mView);
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder (ItemsAdapter.MyViewHolder holder, final int position){
        holder.nama_produk.setText(mmenuslist.get(position).getNama_produk());
        int number = Integer.parseInt(mmenuslist.get(position).getHarga_produk());
        String str = String.format(Locale.US, "%,d", number).replace(',', '.');
        holder.harga_produk.setText("Rp "+str);
        //holder.kategori_produk.setText(mmenuslist.get(position).getKategori_produk());
        Glide.with(holder.itemView.getContext())
                .load(Config.IMAGES_URL + mmenuslist.get(position).getFoto_produk())
                .apply(new RequestOptions().override(350, 550))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.foto_produk);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(view.getContext(), ActivitySelectMenu.class);
                mIntent.putExtra("id_produk", mmenuslist.get(position).getId_produk());
                mIntent.putExtra("nama_produk", mmenuslist.get(position).getNama_produk());
                mIntent.putExtra("kategori_produk", mmenuslist.get(position).getKategori_produk());
                mIntent.putExtra("harga_produk", mmenuslist.get(position).getHarga_produk());
                mIntent.putExtra("tanggal_produk", mmenuslist.get(position).getTanggal_produk());
                mIntent.putExtra("foto_produk", mmenuslist.get(position).getFoto_produk());
                view.getContext().startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mmenuslist.size();
    }

    public final void filtermenu(ArrayList<Products> filteredList){
        mmenuslist  = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_produk, harga_produk, kategori_produk;
        public ImageView foto_produk;

        public MyViewHolder(View itemView) {
            super(itemView);
            nama_produk = (TextView) itemView.findViewById(R.id.namaProduk);
            harga_produk = (TextView) itemView.findViewById(R.id.hargaProduk);
            kategori_produk = (TextView) itemView.findViewById(R.id.kategoriProduk);
            foto_produk = (ImageView) itemView.findViewById(R.id.imgProduk);
        }
    }
}
