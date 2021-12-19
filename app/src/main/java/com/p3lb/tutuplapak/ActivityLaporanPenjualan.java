package com.p3lb.tutuplapak;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lb.tutuplapak.model.Posttrxhbptahun;
import com.p3lb.tutuplapak.model.Posttrxnormaltahun;
import com.p3lb.tutuplapak.model.Trxhbptahun;
import com.p3lb.tutuplapak.model.Trxnormaltahun;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLaporanPenjualan extends AppCompatActivity {
    TextView riwayatTransaksi, totalTransaksi, jumlahTransaksi, totalHBP, totalGross, totalNett, textLaporan;
    Button btnpdf;
    TextView back;
    int year = 0;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAMACABANG = "namacabang";
    String idcabang = "";
    String namapengguna = "";
    String namacabang = "";
    DatePickerDialog dpd;
    String totaltrx = "0";
    String jumlahtrx = "0";
    String totaldiskon = "0";
    String jumlahdiskon = "0";
    String totalrefund = "0";
    String jumlahrefund = "0";
    String totalhbp = "0";
    Bitmap bmp, scaledbmp;
    Date dateObj;
    DateFormat dateFormat;
    int pageWidth = 1200;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_laporan);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        btnpdf = (Button) findViewById(R.id.btnPDF);
        totalTransaksi = (TextView) findViewById(R.id.totalTransaksi);
        back = (TextView) findViewById(R.id.back);
        riwayatTransaksi = (TextView) findViewById(R.id.riwayatTransaksi);
        jumlahTransaksi = (TextView) findViewById(R.id.jumlahTransaksi);
        totalHBP = (TextView) findViewById(R.id.totalHBP);
        textLaporan = (TextView) findViewById(R.id.textLaporan);
        totalGross = (TextView) findViewById(R.id.totalGross);
        totalNett = (TextView) findViewById(R.id.totalNett);
        idcabang = sharedPreferences.getString(KEY_ID, null);
        namapengguna = sharedPreferences.getString(KEY_USERNAME, null);
        namacabang = sharedPreferences.getString(KEY_NAMACABANG, null);
        //bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tutuplapak);
        //scaledbmp = Bitmap.createScaledBitmap(bmp, 1200, 518, false);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        textLaporan.setText("Laporan Penjualan \n TutupLapak Tahun "+year);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLaporanPenjualan.this, ActivityTampilProduk.class);
                startActivity(intent);
            }
        });
        riwayatTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLaporanPenjualan.this, ActivityRiwayatTransaksi.class);
                startActivity(intent);
            }
        });
        createPDF();
    /*    View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);*/
        initdate();
        gettransaksinormal();
        gethbptahun();
    }
    public void initdate(){
         totaltrx = "0";
         jumlahtrx = "0";
         totaldiskon = "0";
         jumlahdiskon = "0";
         totalrefund = "0";
         jumlahrefund = "0";
         totalhbp = "0";
         totalHBP.setText("0");
         jumlahTransaksi.setText("0");
         totalTransaksi.setText("Rp 0");
         totalGross.setText("0");
         totalNett.setText("0");
    }

    public void gettransaksinormal() {
        ApiInterface mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<Posttrxnormaltahun> call = mApiInterface.gettransaksitahun(idcabang,String.valueOf(year));
        call.enqueue(new Callback<Posttrxnormaltahun>() {
            @Override
            public void onResponse(Call<Posttrxnormaltahun> call, Response<Posttrxnormaltahun>
                    response) {

                List<Trxnormaltahun> trxnormaltahunList = response.body().getTrxnormaltahunList();
                totaltrx = trxnormaltahunList.get(0).getTotal_transaksi();
                if(totaltrx==null){
                    totaltrx = "0";
                    Toasty.normal(ActivityLaporanPenjualan.this, "Laporan untuk tahun ini masih belum ada", Toast.LENGTH_SHORT).show();
                }else{
                    jumlahtrx = trxnormaltahunList.get(0).getJumlah_transaksi();
                    if(totaltrx == null){
                        totalTransaksi.setText("0");
                    }
                    int number = Integer.parseInt(totaltrx);
                    String tot = String.format(Locale.US, "%,d", number).replace(',', '.');
                    totalTransaksi.setText("Rp "+tot);
                    jumlahTransaksi.setText(jumlahtrx);
                    totalGross.setText("Rp "+tot);
                }
            }
            @Override
            public void onFailure(Call<Posttrxnormaltahun> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toasty.error(ActivityLaporanPenjualan.this, "Gagal memuat total transaksi  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void gethbptahun() {
        ApiInterface mApiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<Posttrxhbptahun> call = mApiInterface.gettransaksihbptahun(idcabang,String.valueOf(year));
        call.enqueue(new Callback<Posttrxhbptahun>() {
            @Override
            public void onResponse(Call<Posttrxhbptahun> call, Response<Posttrxhbptahun>
                    response) {
                if(response.isSuccessful()){
                    try {
                        List<Trxhbptahun> trxhbptahunList = response.body().getTrxhbptahunList();
                        totalhbp = trxhbptahunList.get(0).getTotal_biayaproduk();

                        int hbp = 0;
                        if(totalhbp.equals("null")){
                            hbp = 0;
                        }else{
                            hbp = Integer.parseInt(totalhbp);
                        }
                        int number = Integer.parseInt(totalhbp);
                        String hb = String.format(Locale.US, "%,d", number).replace(',', '.');
                        totalHBP.setText("Rp " + hb);
                        int total = 0;
                        String s = totaltrx;
                        total = Integer.parseInt(s);
                        int net = total-hbp;
                        Log.d("net", "net " + net);
                        if(total==0){
                            Log.d("cek", ""+total);
                            int hbpbaru = Integer.valueOf(totalHBP.getText().toString().substring(3));
                            int result = Integer.valueOf(totalTransaksi.getText().toString().substring(3));
                            result = result-hbpbaru;
                            int res = result;
                            String ress = String.format(Locale.US, "%,d", res).replace(',', '.');
                            totalNett.setText("Rp "+ress);
                        }else {
                            int res = net;
                            String nettt = String.format(Locale.US, "%,d", res).replace(',', '.');
                            totalNett.setText("Rp " + nettt);
                        }
                        Log.d("totaltrx", "total " + total);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }else{
                    Toasty.normal(ActivityLaporanPenjualan.this, "Gagal memuat laporan  ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Posttrxhbptahun> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toasty.normal(ActivityLaporanPenjualan.this, "Gagal memuat laporan  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void createPDF() {
        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateObj = new Date();
                if (totalTransaksi.getText().toString().equals("Rp 0")){
                    Toasty.normal(ActivityLaporanPenjualan.this, "Data tidak lengkap", Toast.LENGTH_SHORT).show();
                } else {

                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlePaint = new Paint();

                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();

                    //canvas.drawBitmap(scaledbmp, 0, 0, myPaint);
                    // titlePaint.setTextAlign(Paint.Align.CENTER);
                    //titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    //titlePaint.setTextSize(70);
                    //canvas.drawText("Couvee Petshop", pageWidth / 2, 270, titlePaint);

                    myPaint.setColor(Color.rgb(0, 113, 188));
                    myPaint.setTextSize(30f);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Telp. 0895-6146-10359", 1160, 40, myPaint);
                    canvas.drawText("tutuplapak123@gmail.com", 1160, 80, myPaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(60);
                    canvas.drawText("Laporan Penjualan Tahun "+year, pageWidth / 2, 500, titlePaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Nama : "+namapengguna, 20, 640, myPaint);
                    canvas.drawText("Cabang : "+namacabang, 20, 690, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    canvas.drawText("Tanggal : "+dateFormat.format(dateObj), pageWidth-20, 690, myPaint);

//                    myPaint.setStyle(Paint.Style.STROKE);
//                    myPaint.setStrokeWidth(2);
//                    canvas.drawRect(20, 780, pageWidth-20, 860, myPaint);

                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("Keterangan", 150, 870, myPaint);
                    canvas.drawText("Jumlah", 1050, 870, myPaint);


                    //canvas.drawLine(600, 790, 600, 840, myPaint);
                    canvas.drawLine(600, 820, 600, 1320, myPaint);
                    canvas.drawLine(30, 820, 30, 1320, myPaint);
                    canvas.drawLine(1150, 820, 1150, 1320, myPaint);

                    canvas.drawLine(30, 820, 1150, 820, myPaint);
                    canvas.drawLine(30, 920, 1150, 920, myPaint);
                    canvas.drawLine(30, 1320, 1150, 1320, myPaint);

//                    if(item1spinner.getSelectedItemPosition()!=0)
//                    {

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total Transaksi", 70, 950, myPaint);
                    canvas.drawText("Jumlah Transaksi", 70, 1000, myPaint);
                    canvas.drawText("Total Harga Bahan Pokok", 70, 1050, myPaint);
                    canvas.drawText("Pendapatan Kotor", 70, 1100, myPaint);
                    canvas.drawText("Pendapatan Bersih", 70, 1150, myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);

                    canvas.drawText(""+totalTransaksi.getText().toString(), 1100, 950, myPaint);
                    canvas.drawText(""+jumlahTransaksi.getText().toString(), 1100, 1000, myPaint);
                    canvas.drawText(""+totalHBP.getText().toString(), 1100, 1050, myPaint);
                    canvas.drawText(""+totalGross.getText().toString(), 1100, 1100, myPaint);
                    canvas.drawText(""+totalNett.getText().toString(), 1100, 1150, myPaint);
//                    }


                    myPdfDocument.finishPage(myPage1);

                    File file = new File(Environment.getExternalStorageDirectory(), "LaporanTahunanTutupLapak.pdf");

                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        Toasty.success(ActivityLaporanPenjualan.this, "Sukses cetak", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                    myPdfDocument.close();
                }
            }

        });
    }

}
