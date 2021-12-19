package com.p3lb.tutuplapak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lb.tutuplapak.adapter.CartsAdapter;
import com.p3lb.tutuplapak.model.Cart;
import com.p3lb.tutuplapak.model.PostPutDelCart;
import com.p3lb.tutuplapak.model.PostPutDelTransaksi;
import com.p3lb.tutuplapak.model.PostTransaksi;

import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCheckout extends AppCompatActivity{
    TextView totalBayar,totalBayarBayang, Pesanan;
    EditText alamatPembeli;
    Button btnBayar;
    TextView backcart, txtLokasi;
    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ID = "id";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_BAYAR = "totalbayar";
    private static final String KEY_PESANAN = "pesanan";

    String iddetailtransaksi = "0";
    String pesanan = "";
    String idcabang = "";
    String username = "";
    String id_detail = "";
    String alamat = "";
    private int totalbyr = 0;
    public static ActivityCheckout mi;
    List<Cart> cartList ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);
        totalBayar = (TextView) findViewById(R.id.totalBayar);
        totalBayarBayang = (TextView) findViewById(R.id.totalBayarBayang);
        Pesanan = (TextView) findViewById(R.id.pesanan);
        txtLokasi = (TextView) findViewById(R.id.txtLokasi);
        alamatPembeli = (EditText) findViewById(R.id.alamatPembeli);

        btnBayar = (Button) findViewById(R.id.btnBayar);
        //diskon = (EditText) findViewById(R.id.diskon);
        backcart = (TextView) findViewById(R.id.backcart);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        idcabang = sharedPreferences.getString(KEY_ID,null);
        username = sharedPreferences.getString(KEY_USERNAME,null);
        alamat = sharedPreferences.getString(KEY_ALAMAT,null);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Intent mIntent = getIntent();
        id_detail = mIntent.getStringExtra("id_detailtransaksi");
        iddetailtransaksi = mIntent.getStringExtra("iddetailtransaksi");
        mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        mi=this;
        showcart();
        Log.d("Alamat", ""+alamat);
        alamatPembeli.setText(alamat);
        Log.d("ID DETAIL",""+iddetailtransaksi);
//        if(iddetailtransaksi==null){
//            iddetailtransaksi = "0";
//        }
        if(iddetailtransaksi!=null){
            deletecart(iddetailtransaksi);
        }
        backcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCheckout.this, ActivityMenu.class);
                startActivity(intent);
            }
        });
        txtLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCheckout.this, ActivityMaps.class);
                startActivity(intent);
            }
        });
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalBayar.getText().toString().equals("Rp 0") || totalBayar.getText().toString().equals("0")){
                    Toasty.normal(ActivityCheckout.this, "Keranjang masih kosong", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(alamatPembeli.getText().toString().isEmpty() || alamatPembeli.getText().toString().equals("")){
                        Toasty.normal(ActivityCheckout.this, "Harap isi alamat pengiriman barang", Toast.LENGTH_SHORT).show();
                    }else{
                        bayar();
                    }

                }

            }
        });

       /* View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/

    }


    private int totalbayar(List<Cart> cartList) {

        int totalPrice = 0;
        for (int i = 0; i < cartList.size(); i++) {
            int harga = Integer.parseInt(cartList.get(i).getHarga_subtotal());
            totalPrice += harga ;
        }
        return totalPrice;
    }

//    private int diskon(int totalbayar, String minbayar, String persendiskon, String maxdiskon) {
//        int grandtotal = 0;

//    }


    public void showcart() {
        ApiInterface mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PostPutDelCart> call = mApiInterface.getCart(idcabang,username);
        call.enqueue(new Callback<PostPutDelCart>() {
            @Override
            public void onResponse(Call<PostPutDelCart> call, Response<PostPutDelCart>
                    response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                List<Cart> cartList = response.body().getListDataCart();
                Log.d("Retrofit Get", "Jumlah item keranjang: " +
                        String.valueOf(cartList.size()));
                for(Cart cart : cartList){
                    int angka = Integer.parseInt(cart.getHarga_subtotal());
                    String nomi = String.format(Locale.US, "%,d", angka).replace(',', '.');
                    pesanan += ""+cart.getNama_produk()+"\n";
                    pesanan += "x"+cart.getJumlah_item()+"                   Rp "+nomi+"\n";
                    //pesanan += "x"+cart.getJumlah_item()+"                   Rp "+cart.getHarga_subtotal()+"\n";
//                    pesanan += ""+cart.getNama_produk()+" x"+cart.getJumlah_item();
//                    pesanan += "    Rp "+cart.getHarga_subtotal()+" \n";
                }
                Log.d("Pesanan2", pesanan);
                editor.putString(KEY_PESANAN, pesanan);
                totalbyr = totalbayar(cartList);
                int number = totalbyr;
                String str = String.format(Locale.US, "%,d", number).replace(',', '.');
                totalBayarBayang.setText("Rp "+str);
                totalBayar.setText(String.valueOf(totalbyr));
                editor.putString(KEY_BAYAR, String.valueOf(totalbyr));
                Log.d("Bayar2","Bayar "+totalbyr);
                editor.apply();
                mAdapter = new CartsAdapter(cartList);
                //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<PostPutDelCart> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toasty.error(ActivityCheckout.this, "Gagal memuat keranjang  " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    void bayar(){
        String bayartotal = String.valueOf(totalbyr);
        Call<PostTransaksi> postTransaksiCall;
        if(alamatPembeli.getText().toString().isEmpty()){
            postTransaksiCall = mApiInterface.addTransaksi(
                    idcabang,
                    "-",
                    username,
                    bayartotal);
        }else {
            postTransaksiCall = mApiInterface.addTransaksi(
                    idcabang,
                    alamatPembeli.getText().toString(),
                    username,
                    bayartotal);
        }
        postTransaksiCall.enqueue(new Callback<PostTransaksi>() {
            @Override
            public void onResponse(Call<PostTransaksi> call, Response<PostTransaksi> response) {
                if(response.isSuccessful()) {
                    Log.d("RETRO", "ON SUCCESS : " + response.message());
                    Toasty.success(getApplicationContext(), "Transaksi berhasil di proses", Toast.LENGTH_SHORT).show();
                    updatecart();
                    Intent intent = new Intent(ActivityCheckout.this, ActivityMenu.class);
                    startActivity(intent);


                }
                else {
                    Log.d("RETRO", "ON FAIL : " + response.message());
                    Toasty.error(getApplicationContext(), "Transaksi gagal di proses", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<PostTransaksi> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toasty.error(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void updatecart(){
        Call<PostTransaksi> postTransaksiCall;
        if(alamatPembeli.getText().toString().isEmpty()){
            postTransaksiCall = mApiInterface.updatecart(
                    idcabang,
                    "-",
                    username);
        }else{
            postTransaksiCall = mApiInterface.updatecart(
                    idcabang,
                    alamatPembeli.getText().toString(),
                    username);
        }
        postTransaksiCall.enqueue(new Callback<PostTransaksi>() {
            @Override
            public void onResponse(Call<PostTransaksi> call, Response<PostTransaksi> response) {
                if(response.isSuccessful()) {
                    Log.d("RETRO", "ON SUCCESS : " + response.message());
                    Toasty.success(getApplicationContext(), "Update berhasil", Toast.LENGTH_SHORT).show();

                }
                else {
                    Log.d("RETRO", "ON FAIL : " + response.message());
                    Toasty.error(getApplicationContext(), "Update gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostTransaksi> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toasty.success(getApplicationContext(), "gagal di update "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deletecart(String iddelete){
        Call<PostPutDelTransaksi> postPutDelTransaksiCall = mApiInterface.deletecart(
                iddelete,
                idcabang,
                username);
        Log.d("deletelog", ""+iddelete);
        postPutDelTransaksiCall.enqueue(new Callback<PostPutDelTransaksi>() {
            @Override
            public void onResponse(Call<PostPutDelTransaksi> call, Response<PostPutDelTransaksi> response) {
                if(response.isSuccessful()) {
                    Log.d("RETRO", "ON SUCCESS : " + response.message());
                    Toasty.success(getApplicationContext(), "Sukses hapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityCheckout.this, ActivityCheckout.class);
                    startActivity(intent);

                }
                else {
                    Log.d("RETRO", "ON FAIL : " + response.message());
                    Toasty.error(getApplicationContext(), "Gagal hapus", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivityCheckout.this, ActivityCheckout.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PostPutDelTransaksi> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toasty.error(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


}
