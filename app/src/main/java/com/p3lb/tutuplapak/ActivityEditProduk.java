package com.p3lb.tutuplapak;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.p3lb.tutuplapak.model.PostPutDelProducts;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProduk extends AppCompatActivity {
    EditText nama_produk, harga_produk, biaya_produk, jumlah_produk;
    Spinner kategori_produk;
    Button btnSimpan, btnGallery, btnHapus;
    TextView backeditproduk;
    ImageView foto_produk;
    String ID;
    private String mediaPath;
    private String postPath;
    ApiInterface mApiInterface;
    private static final int REQUEST_PICK_PHOTO = Config.REQUEST_PICK_PHOTO;
    private static final int REQUEST_WRITE_PERMISSION = Config.REQUEST_WRITE_PERMISSION;

    private final int ALERT_DIALOG_CLOSE = Config.ALERT_DIALOG_CLOSE;
    private final int ALERT_DIALOG_DELETE = Config.ALERT_DIALOG_DELETE;
    private static final String UPDATE_FLAG = Config.UPDATE_FLAG;

    // Akses Izin Ambil Gambar dari Storage

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateProduk();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produk);

        //Inisialisasi komponen form
        nama_produk = (EditText) findViewById(R.id.nama_produk);
        biaya_produk = (EditText) findViewById(R.id.biaya_produk);
        harga_produk = (EditText) findViewById(R.id.harga_produk);
        jumlah_produk = (EditText) findViewById(R.id.jumlah_produk);
        kategori_produk = (Spinner) findViewById(R.id.spinKategoriProduk);
        foto_produk = (ImageView) findViewById(R.id.foto_produk);
        btnGallery = (Button) findViewById(R.id.btnUploadImage);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        backeditproduk = (TextView) findViewById(R.id.backeditproduk);
        btnHapus = (Button) findViewById(R.id.btnHapus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kategori, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori_produk.setAdapter(adapter);
        //Inisialisasi intent ke komponen form
        Intent mIntent = getIntent();
        ID = mIntent.getStringExtra("id_produk");
        nama_produk.setText(mIntent.getStringExtra("nama_produk"));
        biaya_produk.setText(mIntent.getStringExtra("biaya_produk"));
        harga_produk.setText(mIntent.getStringExtra("harga_produk"));
        jumlah_produk.setText(mIntent.getStringExtra("jumlah_produk"));
        //Input gambar ke imgview
        Glide.with(ActivityEditProduk.this)
                .load(Config.IMAGES_URL + mIntent.getStringExtra("foto_produk"))
                .apply(new RequestOptions().override(350, 550))
                .into(foto_produk);

        // Definisi API
        mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        backeditproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityEditProduk.this, ActivityTampilProduk.class);
                startActivity(intent);
            }
        });
        // Fungsi Tombol Pilih Galery
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
            }
        });

        // Fungsi Tombol Update
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nama_produk.getText().toString().equals("")||nama_produk.getText().toString().isEmpty()){
                    Toasty.warning(getApplicationContext(), "Nama produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    if(biaya_produk.getText().toString().equals("")||biaya_produk.getText().toString().isEmpty()){
                        Toasty.warning(getApplicationContext(), "Biaya produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }else{
                        if(harga_produk.getText().toString().equals("")||harga_produk.getText().toString().isEmpty()){
                            Toasty.warning(getApplicationContext(), "Harga produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        }else{
                            if(jumlah_produk.getText().toString().equals("")||jumlah_produk.getText().toString().isEmpty()){
                                Toasty.warning(getApplicationContext(), "Stok produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            }else{
                                int biaya = Integer.valueOf(biaya_produk.getText().toString());
                                int harga = Integer.valueOf(harga_produk.getText().toString());
                                if(biaya>=harga){
                                    Toasty.warning(getApplicationContext(), "Biaya produk tidak boleh sama atau lebih dari harga produk", Toast.LENGTH_SHORT).show();
                                }else{
                                    updateProduk();
                                }
                            }
                        }
                    }
                }
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusProduk();
            }
        });
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_PICK_PHOTO){
                if(data!=null){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    foto_produk.setImageURI(data.getData());
                    cursor.close();

                    postPath = mediaPath;
                }
            }
        }
    }

    private void updateProduk() {
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (mediaPath== null)
        {
            Toasty.error(getApplicationContext(), "Pilih gambar dulu, baru simpan ...!", Toast.LENGTH_LONG).show();
        }
        else {
            File imagefile = new File(mediaPath);
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
            MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto_produk", imagefile.getName(), reqBody);

            Call<PostPutDelProducts> postHerosCall = mApiInterface.postUpdateProducts(partImage,
                    RequestBody.create(MediaType.parse("text/plain"), ID),
                    RequestBody.create(MediaType.parse("text/plain"), nama_produk.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), biaya_produk.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), harga_produk.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), jumlah_produk.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), kategori_produk.getSelectedItem().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), date),
                    RequestBody.create(MediaType.parse("text/plain"), UPDATE_FLAG));
            postHerosCall.enqueue(new Callback<PostPutDelProducts>() {
                @Override
                public void onResponse(Call<PostPutDelProducts> call, Response<PostPutDelProducts> response) {
                    Log.d("RETRO", "ON SUCCESS : " + response.message());
                    Toasty.success(getApplicationContext(), "Produk berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                    ActivityTampilProduk.ma.tampilproduk();
                    finish();
                }

                @Override
                public void onFailure(Call<PostPutDelProducts> call, Throwable t) {
                    Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                    //Log.d("RETRO", "ON FAILURE : " + t.getCause());
                    Toasty.error(getApplicationContext(), "Produk gagal diperbaharui", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // Cek Versi Android untuk meminta izin
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            updateProduk();
        }
    }
    private void hapusProduk(){
        if (ID.trim().isEmpty()==false){
            Call<PostPutDelProducts> deleteProducts = mApiInterface.deleteProducts(ID);
            deleteProducts.enqueue(new Callback<PostPutDelProducts>() {
                @Override
                public void onResponse(Call<PostPutDelProducts> call, Response<PostPutDelProducts> response) {
                    Toasty.success(getApplicationContext(), "Produk berhasil di hapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityEditProduk.this, ActivityTampilProduk.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<PostPutDelProducts> call, Throwable t) {
                    Toasty.error(getApplicationContext(), "Produk gagal di hapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityEditProduk.this, ActivityTampilProduk.class);
                    startActivity(intent);
                }
            });
        }else {
            Toasty.error(getApplicationContext(), "Produk gagal di hapus", Toast.LENGTH_SHORT).show();
        }
    }
}
