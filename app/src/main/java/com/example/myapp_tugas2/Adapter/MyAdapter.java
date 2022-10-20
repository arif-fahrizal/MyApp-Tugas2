package com.example.myapp_tugas2.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp_tugas2.API.APIRequest;
import com.example.myapp_tugas2.API.RetroServer;
import com.example.myapp_tugas2.Activity.MainActivity;
import com.example.myapp_tugas2.Activity.UpdateActivity;
import com.example.myapp_tugas2.Model.DataModel;
import com.example.myapp_tugas2.Model.ResponseModel;
import com.example.myapp_tugas2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context ctx;
    private List<DataModel> listDataModel;
    private List<DataModel> listData;
    private int id;

    public MyAdapter(Context ctx, List<DataModel> listDataModel) {
        this.ctx = ctx;
        this.listDataModel = listDataModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view  = layoutInflater.inflate(R.layout.activity_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel dm = listDataModel.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
        holder.tvTelepon.setText(dm.getTelepon());
        holder.tvKota.setText(dm.getKota());
    }

    @Override
    public int getItemCount() {
        return listDataModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId,tvNama, tvAlamat, tvTelepon, tvKota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.id);
            tvNama = itemView.findViewById(R.id.nama);
            tvAlamat = itemView.findViewById(R.id.alamat);
            tvTelepon = itemView.findViewById(R.id.telepon);
            tvKota = itemView.findViewById(R.id.kota);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Options");
                    builder.setMessage("Pilih operasi yang akan dilakukan untuk data");
                    builder.setCancelable(true);

                    id = Integer.parseInt(tvId.getText().toString());
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            ctx.startActivity(new Intent(ctx,MainActivity.class));

                        }
                    });
                    builder.show();
                    return false;
                }
            });
        }

        private void deleteData(){
            APIRequest ardData = RetroServer.connectRetrofit().create(APIRequest.class);
            Call<ResponseModel> deleteData = ardData.ardDeleteData(id);

            deleteData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int code = response.body().getCode();
                    String pesan = response.body().getMessage();

                    Toast.makeText(ctx, "Code "+code+" | Message "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
                APIRequest ardData = RetroServer.connectRetrofit().create(APIRequest.class);
                Call<ResponseModel> getData = ardData.ardGetData(id);

                getData.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        int code = response.body().getCode();
                        String pesan = response.body().getMessage();
                        listData = response.body().getData();

                        int varID = listData.get(0).getId();
                        String varNama = listData.get(0).getNama();
                        String varAlamat = listData.get(0).getAlamat();
                        String varTelepon = listData.get(0).getTelepon();
                        String varKota = listData.get(0).getKota();

                        Intent intent = new Intent(ctx, UpdateActivity.class);
                        intent.putExtra("_id", varID);
                        intent.putExtra("_nama", varNama);
                        intent.putExtra("_alamat", varAlamat);
                        intent.putExtra("_telepon", varTelepon);
                        intent.putExtra("_kota", varKota);

                        ctx.startActivity(intent);
                        Toast.makeText(ctx, "Code "+code+" | Message "+pesan, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(ctx, "Gagal menghubungi server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}
