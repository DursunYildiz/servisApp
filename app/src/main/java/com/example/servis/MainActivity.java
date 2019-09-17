package com.example.servis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    FirebaseStorage firebaseStorage;
    DatabaseReference mref;
    LinearLayoutManager mlayoutmanager;
    SharedPreferences sharedPreferences;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Yolcular");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference(firebaseUser.getUid());
        sharedPreferences= getSharedPreferences("sortsetting",MODE_PRIVATE);
        String msortig = sharedPreferences.getString("Sırala","Yeni");

        if (msortig.equals("Yeni")){
            mlayoutmanager = new LinearLayoutManager(this);
            mlayoutmanager.setReverseLayout(true);
            mlayoutmanager.setStackFromEnd(true);
        }
        else if (msortig.equals("Eski")){
            mlayoutmanager = new LinearLayoutManager(this);
            mlayoutmanager.setReverseLayout(false);
            mlayoutmanager.setStackFromEnd(false);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mlayoutmanager);

    }
    private void showDeleteDataDialog(final String currenisim, final String currentimage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Kayıt Sil!");
        builder.setMessage("Kayıtı Silmek İstediğinize Eminmisiniz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = mref.orderByChild("isim").equalTo(currenisim);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(MainActivity.this, "Kayıt Başarı İle Silindi...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                StorageReference mPictureref = getInstance().getReferenceFromUrl(currentimage);
                mPictureref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Fotoğraf Başarı İle Silindi...", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    public void Search(String searchtext) {
        String quary=searchtext.toLowerCase();
        Query firebasequary = mref.orderByChild("isim").startAt(quary).endAt(quary + "\uf0ff");
        FirebaseRecyclerAdapter<Model, Göster> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, Göster>(
                Model.class,
                R.layout.row,
                Göster.class,
                firebasequary
        ) {
            @Override
            protected void populateViewHolder(Göster göster, Model model, int i) {
                göster.setdateail(getApplicationContext(), model.getIsim(), model.getId(), model.getImage(),model.getAçıklama(),model.getTarih());
            }

            @Override
            public Göster onCreateViewHolder(ViewGroup parent, int viewType) {
                Göster göster = super.onCreateViewHolder(parent, viewType);
                göster.setonclick(new Göster.onclickListener() {
                    @Override
                    public void Onitemclick(View view, int position) {

                        String misim =getItem(position).getIsim();
                        String mid =getItem(position).getId();
                        String img =getItem(position).getImage();
                        String açıklama = getItem(position).getAçıklama();
                        String tarih=getItem(position).getTarih();

                        Intent intent = new Intent(view.getContext(), detail.class);

                        intent.putExtra("isim", misim);
                        intent.putExtra("id", mid);
                        intent.putExtra("image", img);
                        intent.putExtra("açıklama", açıklama);
                        intent.putExtra("tarih",tarih);

                        startActivity(intent);
                    }

                    @Override
                    public void onitemlongclick(View view, int position) {
                        String currenisim = getItem(position).getIsim();
                        String currentimage = getItem(position).getImage();
                        showDeleteDataDialog(currenisim,currentimage);
                    }
                });
                return göster;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, Göster> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, Göster>(
                        Model.class,
                        R.layout.row,
                        Göster.class,
                        mref

                ) {
                    @Override
                    protected void populateViewHolder(Göster göster, Model model, int i) {
                        göster.setdateail(getApplicationContext(), model.getIsim(), model.getId(), model.getImage(),model.getAçıklama(),model.getTarih());
                    }

                    @Override
                    public Göster onCreateViewHolder(ViewGroup parent, int viewType) {
                        Göster göster = super.onCreateViewHolder(parent, viewType);
                        göster.setonclick(new Göster.onclickListener() {
                            @Override
                            public void Onitemclick(View view, int position) {

                                String misim =getItem(position).getIsim();
                                String mid =getItem(position).getId();
                                String img =getItem(position).getImage();
                                String açıklama = getItem(position).getAçıklama();
                                String tarih=getItem(position).getTarih();
                                Intent intent = new Intent(view.getContext(), detail.class);

                                // detaylara verileri gönderdik
                                intent.putExtra("isim", misim);
                                intent.putExtra("id", mid);
                                intent.putExtra("image", img);
                                intent.putExtra("açıklama",açıklama);
                                intent.putExtra("tarih",tarih);
                                startActivity(intent);
                            }

                            @Override
                            public void onitemlongclick(View view, int position) {
                                String currenisim = getItem(position).getIsim();
                                String currentimage = getItem(position).getImage();
                                showDeleteDataDialog(currenisim,currentimage);
                            }
                        });
                        return göster;
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newtext) {
                Search(newtext);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (id==R.id.settins){
            showShortdialog();
        }
       else if (id == R.id.item) {
            return true;
        }
       if (id==R.id.hakkında){
           startActivity( new Intent(MainActivity.this,hakkinda.class));


       }

        return super.onOptionsItemSelected(item);
    }

    private void showShortdialog() {
        String [] showoptions = {"Yeni" , "Eski"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sırala").setIcon(R.drawable.ic_action_sort).setItems(showoptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Sırala","Yeni");
            editor.apply();
            recreate();
                }
                else if (i==1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Sırala","Eski");
                    editor.apply();
                    recreate();
                }
            }
        });
        builder.show();

    }


}