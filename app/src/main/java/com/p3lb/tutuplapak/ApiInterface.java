package com.p3lb.tutuplapak;

import com.p3lb.tutuplapak.model.GetProducts;
import com.p3lb.tutuplapak.model.Getnormalbulan;
import com.p3lb.tutuplapak.model.Login;
import com.p3lb.tutuplapak.model.LoginRegisterUsers;
import com.p3lb.tutuplapak.model.PostPutDelCart;
import com.p3lb.tutuplapak.model.PostPutDelProducts;
import com.p3lb.tutuplapak.model.PostPutDelTransaksi;
import com.p3lb.tutuplapak.model.PostTransaksi;
import com.p3lb.tutuplapak.model.PostUser;
import com.p3lb.tutuplapak.model.Posttrxhbptahun;
import com.p3lb.tutuplapak.model.Posttrxnormaltahun;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    //Login dan Registrasi
    @POST("users/login")
    @FormUrlEncoded
    Call<Login> loginUsers(@Field("nama_user") String nama_user,
                           @Field("password_user") String password_user);
    @POST("users/register")
    @FormUrlEncoded
    Call<LoginRegisterUsers> regisUser(@Field("nama_cabang") String nama_cabang,
                                       @Field("nama_user") String nama_user,
                                       @Field("nohp_user") String nohp_user,
                                       @Field("noktp_user") String noktp_user,
                                       @Field("email_user") String email_user,
                                       @Field("password_user") String password_user,
                                       @Field("jabatan_user") int jabatan_user);
    //Produk
    @GET("products")
    Call<GetProducts> getProducts();

    @Multipart
    @POST("products")
    Call<PostPutDelProducts> postProducts(@Part MultipartBody.Part foto_produk,
                                          @Part("nama_produk") RequestBody nama_produk,
                                          @Part("biaya_produk") RequestBody biaya_produk,
                                          @Part("harga_produk") RequestBody harga_produk,
                                          @Part("jumlah_produk") RequestBody jumlah_produk,
                                          @Part("kategori_produk") RequestBody kategori_produk,
                                          @Part("tanggal_produk") RequestBody tanggal_produk,
                                          @Part("flag") RequestBody flag);
    @Multipart
    @POST("products")
    Call<PostPutDelProducts> postUpdateProducts(@Part MultipartBody.Part foto_produk,
                                                @Part("id_produk") RequestBody id_produk,
                                                @Part("nama_produk") RequestBody nama_produk,
                                                @Part("biaya_produk") RequestBody biaya_produk,
                                                @Part("harga_produk") RequestBody harga_produk,
                                                @Part("jumlah_produk") RequestBody jumlah_produk,
                                                @Part("kategori_produk") RequestBody kategori_produk,
                                                @Part("tanggal_produk") RequestBody tanggal_produk,
                                                @Part("flag") RequestBody flag);
    @FormUrlEncoded
    //@HTTP(method = "DELETE", path = "products", hasBody = true)
    @POST("products/hapusproduk")
    Call<PostPutDelProducts> deleteProducts(@Field("id_produk") String id_produk);

    //Transaksi
    @POST("transaksi/historitransaksi")
    @FormUrlEncoded
    Call<Getnormalbulan> historitransaksi(@Field("id_cabang") String id_cabang);

    @POST("detailtransaksi/addcart")
    @FormUrlEncoded
    Call<PostPutDelTransaksi> addcart(@Field("id_produk") String id_produk,
                                      @Field("id_cabang") String id_cabang,
                                      @Field("nama_user") String nama_user,
                                      @Field("jumlah_item") String jumlah_item,
                                      @Field("harga_subtotal") String harga_subtotal,
                                      @Field("nama_pembeli") String nama_pembeli);
    @POST("detailtransaksi")
    @FormUrlEncoded
    Call<PostPutDelCart> getCart(@Field("id_cabang") String id_cabang,
                                 @Field("nama_user") String nama_user);

    @POST("transaksi/bayar")
    @FormUrlEncoded
    Call<PostTransaksi> addTransaksi(@Field("id_cabang") String id_cabang,
                                     @Field("nama_pembeli") String nama_pembeli,
                                     @Field("nama_user") String nama_user,
                                     @Field("total_bayar") String total_bayar);
    @POST("transaksi/updatecart")
    @FormUrlEncoded
    Call<PostTransaksi> updatecart(@Field("id_cabang") String id_cabang,
                                   @Field("nama_pembeli") String nama_pembeli,
                                   @Field("nama_user") String nama_user);
    @POST("detailtransaksi/deletecart")
    @FormUrlEncoded
    Call<PostPutDelTransaksi> deletecart(@Field("id_detailtransaksi") String id_detailtransaksi,
                                         @Field("id_cabang") String id_cabang,
                                         @Field("nama_user") String nama_user);

    //Profile
    @POST("users/profileuser")
    @FormUrlEncoded
    Call<PostUser> profileuser(@Field("id_cabang") String id_cabang,
                               @Field("nama_user") String nama_user);

    @POST("users/updateprofile")
    @FormUrlEncoded
    Call<LoginRegisterUsers> updateprofile(@Field("id_user") String id_user,
                                           @Field("id_cabang") String id_cabang,
                                           @Field("nama_user") String nama_user,
                                           @Field("nohp_user") String nohp_user,
                                           @Field("noktp_user") String noktp_user,
                                           @Field("email_user") String email_user,
                                           @Field("jabatan_user") String jabatan_user);
    //Laporan
    @POST("laporan/gettransaksitahun")
    @FormUrlEncoded
    Call<Posttrxnormaltahun> gettransaksitahun(@Field("id_cabang") String id_cabang,
                                               @Field("tanggal") String tanggal);

    @POST("laporan/gettransaksihbptahun")
    @FormUrlEncoded
    Call<Posttrxhbptahun> gettransaksihbptahun(@Field("id_cabang") String id_cabang,
                                               @Field("tanggal") String tanggal);


}
