package com.p3lb.tutuplapak.model;

import com.google.gson.annotations.SerializedName;

public class History {
    @SerializedName("id_transaksi")
    private String idTransaksi;
    @SerializedName("id_cabang")
    private String idCabang;
    @SerializedName("id_diskon")
    private String idDiskon;
    @SerializedName("nama_pembeli")
    private String namaPembeli;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("total_bayar")
    private String totalBayar;
    @SerializedName("nama_user")
    private String namaUser;
    @SerializedName("status")
    private String status;
    private boolean expanded;

    public History(String idTransaksi, String idCabang, String idDiskon, String namaPembeli, String tanggal, String totalBayar, String namaUser, String status, boolean expanded) {
        this.idTransaksi = idTransaksi;
        this.idCabang = idCabang;
        this.idDiskon = idDiskon;
        this.namaPembeli = namaPembeli;
        this.tanggal = tanggal;
        this.totalBayar = totalBayar;
        this.namaUser = namaUser;
        this.status = status;
        this.expanded = false;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(String idCabang) {
        this.idCabang = idCabang;
    }

    public String getIdDiskon() {
        return idDiskon;
    }

    public void setIdDiskon(String idDiskon) {
        this.idDiskon = idDiskon;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(String totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
