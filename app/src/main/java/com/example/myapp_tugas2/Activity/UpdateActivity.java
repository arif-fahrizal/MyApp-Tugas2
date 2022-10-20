package com.example.myapp_tugas2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp_tugas2.API.APIRequest;
import com.example.myapp_tugas2.API.RetroServer;
import com.example.myapp_tugas2.Model.ResponseModel;
import com.example.myapp_tugas2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {
    EditText EtNama, EtAlamat, EtTelepon, EtKota;
    Button update;
    String nama,alamat,telepon,kota, Gnama,Galamat,Gtelepon,Gkota;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        EtNama = findViewById(R.id.et_nama);
        EtAlamat = findViewById(R.id.et_alamat);
        EtTelepon = findViewById(R.id.et_telepon);
        EtKota = findViewById(R.id.et_kota);
        update = findViewById(R.id.update_button);

        id = getIntent().getIntExtra("_id", -1);
        nama = getIntent().getStringExtra("_nama");
        alamat = getIntent().getStringExtra("_alamat");
        telepon = getIntent().getStringExtra("_telepon");
        kota = getIntent().getStringExtra("_kota");

        EtNama.setText(nama);
        EtAlamat.setText(alamat);
        EtTelepon.setText(telepon);
        EtKota.setText(kota);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gnama = EtNama.getText().toString();
                Galamat = EtAlamat.getText().toString();
                Gtelepon = EtTelepon.getText().toString();
                Gkota = EtKota.getText().toString();
                updateData();
                startActivity(new Intent(UpdateActivity.this, MainActivity.class));
            }
        });
    }
    private void updateData() {
        APIRequest ardData = RetroServer.connectRetrofit().create(APIRequest.class);
        Call<ResponseModel> updateData = ardData.ardUpdateData(id, Gnama, Galamat, Gtelepon, Gkota);

        updateData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getMessage();

                Toast.makeText(UpdateActivity.this, "Code : " + code + " | Message : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Failed to Update Data | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}