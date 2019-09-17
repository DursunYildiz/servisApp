package com.example.servis;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class detail extends AppCompatActivity {
  TextView misim , maçıklama,textarih;
  ImageView ımageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detaylar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
         misim =(TextView)findViewById(R.id.misim);
        maçıklama =(TextView)findViewById(R.id.textView4);
        ımageView =(ImageView)findViewById(R.id.image51);
        textarih=(TextView) findViewById(R.id.textView2);

        String isim = getIntent().getStringExtra("isim");

        String tarih=getIntent().getStringExtra("tarih");
        String image= getIntent().getStringExtra("image");

         String açıklama=getIntent().getStringExtra("açıklama");

        misim.setText(isim);

        maçıklama.setText("Açıklama: "+açıklama);
        textarih.setText("Eklenme Zamanı: "+tarih);

        Picasso.get().load(image).into(ımageView);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
