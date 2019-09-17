package com.example.servis;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class ekleme2 extends AppCompatActivity {

       private ImageView foto;

       private Button butonkaydet;

       private EditText editTextisim,editTextid,editTextaçıklama;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eklemetest);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Kayıt Ekranı");
        butonkaydet=(Button)findViewById(R.id.button7);

        foto=(ImageView)findViewById(R.id.imageView2);
      editTextaçıklama=(EditText)findViewById(R.id.editText3);
        editTextid=(EditText) findViewById(R.id.editTextid);
        editTextisim=(EditText) findViewById(R.id.editTextisim);
        storageReference = FirebaseStorage.getInstance().getReference("fotoğraflar2");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();



        progressDialog = new ProgressDialog(ekleme2.this);

foto.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(Intent.createChooser(intent, "Fotoğraf Seç"), Image_Request_Code);
    }
});
butonkaydet.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        UploadImage();
    }
});

           }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                foto.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Kayıt Ediliyor...!");
            progressDialog.show();
            //
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           String açıklama=editTextaçıklama.getText().toString().trim();
                            String isim = editTextisim.getText().toString().trim();
                            String id =editTextid.getText().toString().trim();
                            progressDialog.dismiss();

                            @SuppressWarnings("VisibleForTests")
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                           while (!task.isComplete());
                            Uri dwl= task.getResult();
                            String imgurl = dwl.toString();
                      String  de=DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            databaseReference=FirebaseDatabase.getInstance().getReference(id);
                            Model imageUploadInfo = new Model(isim,id, imgurl,açıklama,de);

                            databaseReference.child(isim).setValue(imageUploadInfo);
                            if (task.isSuccessful()){
                                if (isim==null){
                                    Toast.makeText(ekleme2.this, "İsim boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                                }
                                else  if (id==null){
                                    Toast.makeText(ekleme2.this, "Boşlukları Doldurun!", Toast.LENGTH_SHORT).show();
                                }
                                else    Toast.makeText(ekleme2.this, "Kayıt Eklendi", Toast.LENGTH_SHORT).show();



                            }
                        }
                    });
        }
        else {

            Toast.makeText(ekleme2.this, "Lütfen Boş Alanları Doldurun!", Toast.LENGTH_LONG).show();

        }
    }
}
