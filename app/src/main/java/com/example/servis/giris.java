package com.example.servis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.subhrajyoti.passwordview.PasswordView;

public class giris extends AppCompatActivity {
Button gir,kayıt,yolcu;
EditText email ,şifree,editemail,editşifre;
PasswordView passwordView;
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        passwordView =(PasswordView) findViewById(R.id.passwordView);
        gir =(Button)findViewById(R.id.button3);
        email=(EditText)findViewById(R.id.etEmail);

        yolcu=(Button) findViewById(R.id.button4);
        firebaseAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Giriş Ekranı");
        editemail=(EditText)findViewById(R.id.etEmail);

             task();

        yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ekleme2.class);
                startActivity(intent);
            }
        });

        gir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isconnected()){
                    new AlertDialog.Builder(giris.this).setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("İnternet Bağlantısı Bulunamadı!!")
                            .setMessage("Uygulama İnternet Bağlantısı Olmadan Kullanılamaz!! Lütfen Bağlantınızı Kontrol Edin!! ")
                            .setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }

                String email =editemail.getText().toString().trim();
                String şifre =passwordView.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(giris.this, "Lütfen email giriniz!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(şifre)){
                    Toast.makeText(giris.this, "Lütfen şifre giriniz!", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, şifre)
                        .addOnCompleteListener(giris.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                   startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                } else {
                                    Toast.makeText(giris.this, "Giriş başarısız! Email veya Şifre Hatalı!", Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });

            }
        });

    }
    public boolean isconnected (){
        ConnectivityManager connectivityManager = ( ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null && networkInfo.isConnected() ;
    }
    public  void  task (){
        if (!isconnected()){
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("İnternet Bağlantısı Bulunamadı!!")
                    .setMessage("Uygulama İnternet Bağlantısı Olmadan Kullanılamaz!! Lütfen Bağlantınızı Kontrol Edin!! ")
                    .setPositiveButton("Tekrar Deneyin!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                             task();
                            // finish();
                        }
                    }).show();
        }
    }
}
