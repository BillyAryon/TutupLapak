package com.p3lb.tutuplapak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.p3lb.tutuplapak.adapter.ProductsAdapter;
import com.p3lb.tutuplapak.model.GetProducts;
import com.p3lb.tutuplapak.model.Products;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTampilProduk extends AppCompatActivity {
    ApiInterface mApiInterface;
    Button btnLaporan;
    TextView btnTambahProduk, txtLogout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static ActivityTampilProduk ma;
    List<Products> productsList;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dataproduk);
        btnTambahProduk = findViewById(R.id.btnTambahProduk);
        txtLogout = findViewById(R.id.txtLogout);
        btnLaporan = findViewById(R.id.btnLaporan);
        //backlistmenu = (Button) findViewById(R.id.backlistmenu);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_produk);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        ma=this;

        tampilproduk();
        btnLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTampilProduk.this, ActivityLaporanPenjualan.class);
                startActivity(intent);
            }
        });
        btnTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTampilProduk.this, ActivityTambahProduk.class);
                startActivity(intent);
            }
        });
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =getSharedPreferences("mypref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Toasty.success(ActivityTampilProduk.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(ActivityTampilProduk.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
       /* View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/
    }

    public void tampilproduk() {
        ApiInterface mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<GetProducts> call = mApiInterface.getProducts();
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts>
                    response) {
                        List<Products> productsList = response.body().getListDataProducts();
                        mAdapter = new ProductsAdapter(productsList);
                        mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toasty.error(ActivityTampilProduk.this, "Gagal memuat produk  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
