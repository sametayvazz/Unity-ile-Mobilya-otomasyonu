package com.example.mobilyaotomasyon;

public class SA_Urunitem {
    public String SA_isim;
    public String SA_fiyat;
    public String SA_aciklama;
    public String SA_kalite;
    public String SA_renk;
    public String SA_sehir;
    public String SA_url;

    public SA_Urunitem(){}

    public SA_Urunitem(String isim, String yas, String cins, String kalite, String renk, String sehir, String url) {
        this.SA_isim = isim;
        this.SA_fiyat = yas;
        this.SA_aciklama = cins;
        this.SA_kalite = kalite;
        this.SA_renk = renk;
        this.SA_sehir = sehir;
        this.SA_url = url;
    }
}
