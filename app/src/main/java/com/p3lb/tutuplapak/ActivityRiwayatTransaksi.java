package com.p3lb.tutuplapak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lb.tutuplapak.adapter.HistoriTranksaksiAdapter;
import com.p3lb.tutuplapak.model.Getnormalbulan;
import com.p3lb.tutuplapak.model.History;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRiwayatTransaksi extends AppCompatActivity {
    TextView backhistory;
    ApiInterface mApiInterface;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String KEY_ID = "id";
    String idcabang = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_riwayat);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        backhistory = (TextView) findViewById(R.id.backhistory);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_historitransaksi);
        idcabang = sharedPreferences.getString(KEY_ID, null);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Log.d("coba1", idcabang);
        riwayattransaksi();
        backhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRiwayatTransaksi.this, ActivityLaporanPenjualan.class);
                startActivity(intent);
            }
        });
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/

    }

    public void riwayattransaksi() {
        ApiInterface mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<Getnormalbulan> call = mApiInterface.historitransaksi(idcabang);
        call.enqueue(new Callback<Getnormalbulan>() {
            @Override
            public void onResponse(Call<Getnormalbulan> call, Response<Getnormalbulan>
                    response) {
                if(response.isSuccessful()){
                    List<History> historyList = response.body().getHistoryList();
                    Log.d("coba", historyList.get(0).getIdCabang());
                    mAdapter = new HistoriTranksaksiAdapter(historyList);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    Toasty.normal(ActivityRiwayatTransaksi.this, "Gagal memuat riwayat transaksi  ", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Getnormalbulan> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toasty.error(ActivityRiwayatTransaksi.this, "Gagal memuat riwayat transaksi  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
