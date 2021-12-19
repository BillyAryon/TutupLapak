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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.p3lb.tutuplapak.adapter.MenusAdapter;
import com.p3lb.tutuplapak.model.GetProducts;
import com.p3lb.tutuplapak.model.Products;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMenu extends AppCompatActivity{
    ApiInterface mApiInterface;
    TextView profile;
    MenusAdapter menusAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    TextView textView;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ID = "id";
    private static final String KEY_TRX = "trx";
    private static final String KEY_JABATAN = "jabatan";
    private static final String KEY_TOTALBAYAR = "totalbayardiskon";
    private static final String KEY_BAYAR = "totalbayar";
    private static final String KEY_NAMACABANG = "namacabang";
    private static final String KEY_PESANAN = "pesanan";
    private static final String KEY_DISKON = "diskon";
    String keybayar = "";
    String keytotalbayar = "";
    String diskon = "";
    String namacabang = "";
    String nama_user = "";
    String id_username = "";
    String role = "";
    TextView placeholderUsername;
    public static ActivityMenu me;
    Button fltKeranjangBelanja;
    List<Products> productsList;
    String idtransaksi = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        textView = (TextView) findViewById(R.id.textView);
        fltKeranjangBelanja = findViewById(R.id.btnKeranjangBelanja);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
       // mLayoutManager = new LinearLayoutManager(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        nama_user = sharedPreferences.getString(KEY_USERNAME,null);
        id_username = sharedPreferences.getString(KEY_ID,null);
        role = sharedPreferences.getString(KEY_JABATAN,null);
        namacabang = sharedPreferences.getString(KEY_NAMACABANG, null);
        keybayar = sharedPreferences.getString(KEY_BAYAR,null);
        keytotalbayar = sharedPreferences.getString(KEY_TOTALBAYAR,null);
        diskon = sharedPreferences.getString(KEY_DISKON,null);
        placeholderUsername = (TextView) findViewById(R.id.placeholderUsername);
        profile = (TextView) findViewById(R.id.profile);
        placeholderUsername.setText(""+nama_user.toUpperCase());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        //me=this;
        productsList =  new ArrayList<>();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMenu.this, ActivityProfile.class);
                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =getSharedPreferences("mypref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Toasty.success(ActivityMenu.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(ActivityMenu.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
        fltKeranjangBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMenu.this, ActivityCheckout.class);
                startActivity(intent);
            }
        });
        tampilmenu();
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/
    }

    public void tampilmenu() {
        ApiInterface mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<GetProducts> call = mApiInterface.getProducts();
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts>
                    response) {
                List<Products> menuList = response.body().getListDataProducts();
                mAdapter = new MenusAdapter(menuList);
                mRecyclerView.setAdapter(mAdapter);

            }
            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toasty.error(ActivityMenu.this, "Gagal memuat menu  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
