package com.example.myapp_tugas2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_tugas2.API.APIRequest;
import com.example.myapp_tugas2.API.RetroServer;
import com.example.myapp_tugas2.Model.ResponseModel;
import com.example.myapp_tugas2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {

    EditText et_nama, et_alamat, et_telepon, et_kota;
    Button add;
    String nama, alamat, telepon, kota;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_nama = findViewById(R.id.et_nama);
        et_alamat = findViewById(R.id.et_alamat);
        et_telepon = findViewById(R.id.et_telepon);
        et_kota = findViewById(R.id.et_kota);
        add = findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = et_nama.getText().toString();
                alamat = et_alamat.getText().toString();
                telepon = et_telepon.getText().toString();
                kota = et_kota.getText().toString();

                if(nama.trim().equals("")){
                    et_nama.setError("Nama harus diisi");
                }
                else if(alamat.trim().equals("")){
                    et_alamat.setError("Alamat harus diisi");
                }
                else if(telepon.trim().equals("")){
                    et_telepon.setError("Nomor Telepon harus diisi");
                }
                else if(kota.trim().equals("")){
                    et_kota.setError("Nomor Nisn harus diisi");
                }
                else{
                    createData();
                }
                startActivity(new Intent(CreateActivity.this, MainActivity.class));
            }
        });
    }

    private void createData(){
        APIRequest ardData = RetroServer.connectRetrofit().create(APIRequest.class);
        Call<ResponseModel> saveData = ardData.ardCreateData(nama,alamat,telepon,kota);

        saveData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getMessage();

                Toast.makeText(CreateActivity.this, "Code : "+code+" | Message : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(CreateActivity.this, "Failed to Add Data | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
