package com.p3lb.tutuplapak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.p3lb.tutuplapak.model.Login;
import com.p3lb.tutuplapak.model.LoginUsers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity{
    EditText username_login, password_login;
    TextView txtRegistrasi;
    Button btnLogin;
    private String secretKeys = "tutuplapak";
    Config config = new Config();
    SharedPreferences sharedPreferences;
    ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ID = "id";
    private static final String KEY_JABATAN = "jabatan";
    private static final String KEY_NAMACABANG = "namacabang";
    String jabatan = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_login = findViewById(R.id.username_login);
        password_login = findViewById(R.id.password_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegistrasi = (TextView) findViewById(R.id.txtRegistrasi);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        jabatan = sharedPreferences.getString(KEY_JABATAN,null);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username_login.getText().toString().isEmpty() || password_login.getText().toString().isEmpty()) {
                    Toasty.error(ActivityLogin.this, "Lengkapi data untuk login", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Login();
                }
            }
        });
        txtRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegistrasi.class);
                startActivity(intent);
            }
        });
      /*  txtRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, RegistrasiUser.class);
                startActivity(intent);
            }
        });*/

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        decorView.setSystemUiVisibility(uiOptions);
    }

    public static SecretKey generateKey(String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeySpec secret;
        secret = new SecretKeySpec(key.getBytes(), "AES");
        return  secret;
    }

    private void Login(){
            Call<Login> postUsersCall = apiInterface.loginUsers(username_login.getText().toString(), password_login.getText().toString());
            postUsersCall.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if(response.isSuccessful()) {
                        List<LoginUsers> usersList = response.body().getLoginUsers();
                        String namacabang = usersList.get(0).getNama_cabang();
                        String idcabang = usersList.get(0).getId_cabang();
                        String jabatan = usersList.get(0).getJabatan_user();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_USERNAME,username_login.getText().toString());
                        editor.putString(KEY_ID,idcabang);
                        editor.putString(KEY_NAMACABANG,namacabang);
                        editor.putString(KEY_JABATAN,jabatan);
                        editor.apply();
                        if(jabatan.equals("1")){
                            Toasty.success(getApplicationContext(), "Selamat datang kembali "+username_login.getText().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityLogin.this, ActivityTampilProduk.class);
                            startActivity(intent);
                        }
                        if(jabatan.equals("2")){
                            Toasty.success(getApplicationContext(), "Selamat datang kembali "+username_login.getText().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityLogin.this, ActivityMenu.class);
                            startActivity(intent);
                        }

                    }
                    else {
                        Log.d("RETRO", "ON FAIL : " + response.message());
                        Toasty.error(getApplicationContext(), "Login Gagal : Pastikan user telah terdaftar dan sudah dikonfirmasi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                    Toasty.error(getApplicationContext(), "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}



