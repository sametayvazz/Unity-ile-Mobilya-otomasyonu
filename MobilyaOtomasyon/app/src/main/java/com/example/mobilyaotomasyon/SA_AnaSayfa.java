package com.example.mobilyaotomasyon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SA_AnaSayfa extends AppCompatActivity {
    FirebaseAuth SA_mUser;
    RecyclerView SA_analiste;
    ArrayList<SA_Urunitem>  SA_urunListe;
    FirebaseFirestore SA_db;
    SA_UrunAdapter SA_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sa_activity_main);
        SA_mUser = FirebaseAuth.getInstance();
        SA_analiste = findViewById(R.id.SA_recyclerView);
        SA_urunListe = new ArrayList<>();
        SA_db = FirebaseFirestore.getInstance();
        SA_adapter = new SA_UrunAdapter(SA_urunListe);
        SA_analiste.setHasFixedSize(true);
        SA_analiste.setLayoutManager(new LinearLayoutManager(SA_AnaSayfa.this));
        SA_analiste.setAdapter(SA_adapter);

        tumVeriyiGetir();
    }

    public void tumVeriyiGetir(){


        SA_db.collection("SA_MobilyaOtomasyon").orderBy("SA_isim", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getUrunler", document.getId() + " => " + document.getData());
                                Log.d("getUrunler", document.get("SA_isim").toString());

                                SA_urunListe.add(new SA_Urunitem(document.get("SA_isim").toString(),
                                        document.get("SA_fiyat").toString(),
                                        document.get("SA_aciklama").toString(),
                                        document.get("SA_kalite").toString(),
                                        document.get("SA_renk").toString(),
                                        document.get("SA_sehir").toString(),
                                        document.get("SA_url").toString()));
                            }

                            Log.d("getUrunler", SA_urunListe.size()+"");

                            SA_adapter.notifyDataSetChanged();
                        } else {
                            Log.d("getUrunler", "Mobilya ürünlerini sunucudan çekerken bir hata oluştu", task.getException());
                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sa_kullanici_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.yeniurun:
                Toast.makeText(getApplicationContext(),"Yeni Ürün Ekleme Sayfasınza Yönlendirildiniz.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(SA_AnaSayfa.this, SA_YeniUrunEKle.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}