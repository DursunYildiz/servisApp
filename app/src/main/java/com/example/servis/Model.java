package com.example.servis;

public class Model {
    String İsim, Image,İd,Açıklama,Tarih;
    public Model(String isim, String id,String image,String açıklama,String tarih){
        this.İsim=isim;
        this.İd=id;
        this.Tarih=tarih;
        this.Image=image;

        this.Açıklama=açıklama;

    }

     public Model(){}

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    public String getIsim() {
        return İsim;
    }

    public void setIsim(String isim) {
        İsim = isim;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getId() {
        return İd;
    }

    public void setId(String id) {
        İd = id;
    }

    public String getAçıklama() {
        return Açıklama;
    }

    public void setAçıklama(String açıklama) {
        Açıklama = açıklama;
    }
}