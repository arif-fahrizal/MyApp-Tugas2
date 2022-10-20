package com.example.myapp_tugas2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapp_tugas2.API.APIRequest;
import com.example.myapp_tugas2.API.RetroServer;
import com.example.myapp_tugas2.Adapter.MyAdapter;
import com.example.myapp_tugas2.Model.DataModel;
import com.example.myapp_tugas2.Model.ResponseModel;
import com.example.myapp_tugas2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcView;
    FloatingActionButton add;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<DataModel> listDataModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcView = findViewById(R.id.rcview);
        rcView.setAdapter(myAdapter);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        add = findViewById(R.id.floatingActionAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });
       /*layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);*/

        retrieveData();
    }

    public void retrieveData(){
        APIRequest ardData = RetroServer.connectRetrofit().create(APIRequest.class);
        Call<ResponseModel> tampilData = ardData.ardRetriveData();

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getMessage();
                listDataModel = response.body().getData();

//                Toast.makeText(MainActivity.this, "code "+code+" message "+pesan, Toast.LENGTH_SHORT).show();

                myAdapter = new MyAdapter(MainActivity.this, listDataModel);
                rcView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed Connection to Server"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}