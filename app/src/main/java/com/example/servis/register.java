package com.example.servis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText editemail,editşifre,editşifreonay;
    Button button;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();
        editemail=(EditText)findViewById(R.id.editTextmail);
        editşifre=(EditText)findViewById(R.id.editTextşifre);
        editşifreonay=(EditText)findViewById(R.id.editTextşifreonay);
        button=(Button)findViewById(R.id.button2);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =editemail.getText().toString().trim();
                String şifre =editşifre.getText().toString().trim();
                String şifreonay=editşifreonay.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(register.this, "Lütfen email giriniz!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(şifre)){
                    Toast.makeText(register.this, "Lütfen şifre giriniz!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(şifreonay)){
                    Toast.makeText(register.this, "Lütfen şifre onayı giriniz!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (şifre.length()<8){
                    Toast.makeText(register.this, "Şifre minimum 8 basamaklı olmalıdır!", Toast.LENGTH_SHORT).show();
                }
               progressBar.setVisibility(View.VISIBLE);
                //progressDialog.setTitle("Kayıt Ediliyor!...");
                //progressDialog.show();
                if (şifre.equals(şifreonay)){
                    mAuth.createUserWithEmailAndPassword(email, şifre)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                   progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                //        progressDialog.dismiss();
                                 //  startActivity(new Intent(getApplicationContext(), register.class));
                                        Toast.makeText(register.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(register.this, "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });


                }
            }
             });
    }

}
