package com.example.mobilyaotomasyon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SA_YeniUrunEKle extends AppCompatActivity {
    ImageView SA_fotoyeri;
    EditText SA_isim, SA_fiyat, SA_aciklama;
    Button SA_urunTanimlama;
    Bitmap SA_fotom;
    Uri SA_fotoData;
    FirebaseStorage SA_storage;
    FirebaseFirestore SA_veritabani;
    String SA_fotoURL;
    Spinner SA_urun_kalite;
    String SA_renksecilen;
    StringBuilder SA_sb;
    RadioGroup SA_rgRenk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sa_activity_yeni_urun_ekle);
        SA_fotoyeri = findViewById(R.id.SA_iv_ekle_urunfoto);
        SA_isim = findViewById(R.id.SA_etxt_ekle_isim);
        SA_fiyat = findViewById(R.id.SA_etxt_ekle_fiyat);
        SA_aciklama = findViewById(R.id.SA_etxt_ekle_aciklama);
        SA_urunTanimlama = findViewById(R.id.SA_btn_ekle_urungir);
        SA_storage = FirebaseStorage.getInstance();
        SA_veritabani = FirebaseFirestore.getInstance();
        SA_urun_kalite=findViewById(R.id.SA_spinner);
        SA_rgRenk=findViewById(R.id.SA_radyogrup_renk);
        List<String> SA_spinnerElemanlari=new ArrayList<>();
        SA_spinnerElemanlari.add("A Kalite ");
        SA_spinnerElemanlari.add("B Kalite ");
        SA_spinnerElemanlari.add("C Kalite ");

        ArrayAdapter<String> SA_adapter=new ArrayAdapter(SA_YeniUrunEKle.this,
                android.R.layout.simple_spinner_item,SA_spinnerElemanlari);
        SA_urun_kalite.setAdapter(SA_adapter);

        SA_urunTanimlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 urunTanimla_click(view);
            }
        });



        SA_fotoyeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fotoSec();
            }
        });

        Button SA_ana = findViewById(R.id.SA_btn_anasayfa);
        SA_ana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SA_YeniUrunEKle.this, SA_AnaSayfa.class));
                finish();
            }
        });

    }

    public void fotoSec(){
        Toast.makeText(SA_YeniUrunEKle.this,"foto seç çağrıldı",
                Toast.LENGTH_SHORT).show();
        if(ContextCompat.checkSelfPermission(SA_YeniUrunEKle.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
           != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SA_YeniUrunEKle.this, new String[]{Manifest.permission.
                    READ_EXTERNAL_STORAGE},60);
        }else{
            Intent SA_intentfoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(SA_intentfoto,61);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode==60){
            if(grantResults.length>0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                Intent SA_intentfoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(SA_intentfoto,61);
            }
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {

        if(requestCode == 61 && resultCode == RESULT_OK &&
                data != null){
            SA_fotoData  = data.getData();

            try {

                SA_fotom = MediaStore.Images.Media.getBitmap(this.getContentResolver(),SA_fotoData);
                SA_fotoyeri.setImageBitmap(SA_fotom);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void urunTanimla_click(View view) {
        int SA_rbRenk=SA_rgRenk.getCheckedRadioButtonId();
        RadioButton SA_radyorenk=findViewById(SA_rbRenk);
        SA_renksecilen=SA_radyorenk.getText().toString();

        CheckBox SA_ankara=findViewById(R.id.SA_checkBox_ankara);
        CheckBox SA_istanbul=findViewById(R.id.SA_checkBox_istanbul);
        CheckBox SA_tokat=findViewById(R.id.SA_checkBox_tokat);

        SA_sb=new StringBuilder();
        if (SA_ankara.isChecked())
            SA_sb.append("Ankara ");
        if (SA_istanbul.isChecked())
            SA_sb.append("İstanbul ");
        if (SA_tokat.isChecked())
            SA_sb.append("Tokat ");

        StorageReference SA_storageRef = SA_storage.getReference();
        long SA_zaman = System.nanoTime();
        StorageReference mountainImagesRef = SA_storageRef.child("SA_Mobilya/img"+
                String.valueOf(SA_zaman)+".jpg");

        Bitmap SA_bitmap = ((BitmapDrawable) SA_fotoyeri.getDrawable()).getBitmap();
        ByteArrayOutputStream SA_baos = new ByteArrayOutputStream();
        SA_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, SA_baos);
        byte[] SA_data = SA_baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(SA_data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SA_YeniUrunEKle.this, "Foto yüklenirken sorun oluştu", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            SA_fotoURL = uri.toString();
                            Toast.makeText(SA_YeniUrunEKle.this,"Foto başarılı şekilde yüklendi",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("URL", SA_fotoURL);
                            Log.d("URL", SA_isim.getText().toString());
                            Log.d("URL", SA_fiyat.getText().toString());
                            yeniUrunKaydet(SA_isim.getText().toString(),
                                    SA_fiyat.getText().toString(),SA_aciklama.getText().toString(),
                                    SA_urun_kalite.getSelectedItem().toString(),SA_renksecilen,
                                    SA_sb.toString(),SA_fotoURL
                                    );
                        }
                    });
                }





            }
        });




    }
    private void yeniUrunKaydet(String isim, String fiyat, String aciklama,String kalite,String renk,String sehir, String fotoURL) {
        Map<String, Object> yeniUrun = new HashMap<>();
        yeniUrun.put("SA_isim", isim);
        yeniUrun.put("SA_fiyat", fiyat);
        yeniUrun.put("SA_aciklama", aciklama);
        yeniUrun.put("SA_kalite", kalite);
        yeniUrun.put("SA_renk", renk);
        yeniUrun.put("SA_sehir", sehir);
        yeniUrun.put("SA_url", fotoURL);


        SA_veritabani.collection("SA_MobilyaOtomasyon")
                .add(yeniUrun)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("yeni veri", "yeni veri eklenmiştir " + documentReference.getId());
                        Toast.makeText(SA_YeniUrunEKle.this,"veriler kayıt edildi",
                                Toast.LENGTH_SHORT).show();

                        SA_fotoyeri.setImageResource(R.mipmap.ic_launcher);
                        SA_isim.setText("");
                        SA_fiyat.setText("");
                        SA_aciklama.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("yeni veri", "Beklenmeyen bir hata oluştu", e);
                    }
                });

    }
}