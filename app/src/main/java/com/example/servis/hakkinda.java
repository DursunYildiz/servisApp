package com.example.servis;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class hakkinda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hakkinda);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hakkında");
    }
}
