package com.p3lb.tutuplapak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.p3lb.tutuplapak.model.LoginRegisterUsers;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRegistrasi extends AppCompatActivity {
    EditText nama_user, noktp_user, password_user, id_cabang, nohp_user, email_user;
    Button btnRegister;
    TextView txtLogin;
    ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        nama_user = (EditText) findViewById(R.id.nama_user);
        email_user = (EditText) findViewById(R.id.email_user);
        nohp_user = (EditText) findViewById(R.id.nohp_user);
        noktp_user = (EditText) findViewById(R.id.noktp_user);
        password_user = (EditText) findViewById(R.id.password_user);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegistrasi.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama_user.getText().toString().isEmpty() ||
                        password_user.getText().toString().isEmpty() ||
                        email_user.getText().toString().isEmpty() ||
                        nohp_user.getText().toString().isEmpty() ||
                        noktp_user.getText().toString().isEmpty())
                {
                        Toasty.error(ActivityRegistrasi.this, "Lengkapi data untuk registrasi", Toast.LENGTH_SHORT).show();
                        return;
                }else{
                        Registrasi();
                }
            }
        });
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/
    }

    void Registrasi(){
        Call<LoginRegisterUsers> postUsersCall = apiInterface.regisUser(
                "1",
                nama_user.getText().toString(),
                nohp_user.getText().toString(),
                noktp_user.getText().toString(),
                email_user.getText().toString(),
                password_user.getText().toString(),
                2);

        postUsersCall.enqueue(new Callback<LoginRegisterUsers>() {
            @Override
            public void onResponse(Call<LoginRegisterUsers> call, Response<LoginRegisterUsers> response) {
                if(response.isSuccessful()) {
                    Log.d("RETRO", "ON SUCCESS : " + response.message());
                    Toasty.success(getApplicationContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityRegistrasi.this, ActivityLogin.class);
                    startActivity(intent);
                }
                else {
                    Log.d("RETRO", "ON FAIL : " + response.message());
                    Toasty.error(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivityRegistrasi.this, ActivityLogin.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterUsers> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toasty.error(getApplicationContext(), "Error "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
