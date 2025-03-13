package com.example.praktikum3_1;

import java.io.Serializable;

public class Product implements Serializable {
    private String kode;
    private String merk;
    private String kategori;
    private String satuan;
    private double hargajual;
    private double diskonjual;
    private int stok;
    private String foto;
    private String deskripsi;

    // Getters
    public String getKode() {
        return kode;
    }

    public String getMerk() {
        return merk;
    }

    public String getKategori() {
        return kategori;
    }

    public String getSatuan() {
        return satuan;
    }

    public double getHargajual() {
        return hargajual;
    }

    public double getDiskonjual() {
        return diskonjual;
    }

    public int getStok() {
        return stok;
    }

    public String getFoto() {
        return foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    // Format currency
    public double getDiscountedPrice() {
        return hargajual - (hargajual * (diskonjual / 100));
    }

    // Format stock display with unit
    public String getFormattedStock() {
        return String.format("Stock: %d %s", stok, satuan);
    }

    // Format category display
    public String getFormattedCategory() {
        return String.format("Category: %s", kategori);
    }

    // Format unit display
//    public String getFormattedUnit() {
//        return String.format("Unit: %s", satuan);
//    }
}