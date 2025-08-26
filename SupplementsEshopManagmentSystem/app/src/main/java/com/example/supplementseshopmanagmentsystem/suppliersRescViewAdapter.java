package com.example.supplementseshopmanagmentsystem;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class suppliersRescViewAdapter extends RecyclerView.Adapter<suppliersRescViewAdapter.MySupViewHolder> {

    Context context;
    List<SupplierRDB> suppliers;
    List<SuppliesProductRDB> suppliesProduct;
    ProSupDB db;

    public suppliersRescViewAdapter(Context context, List<SupplierRDB> suppliers, List<SuppliesProductRDB> suppliesProduct) {
        this.context = context;
        this.suppliers = suppliers;
        this.suppliesProduct=suppliesProduct;
    }

    @NonNull
    @Override
    public suppliersRescViewAdapter.MySupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.supplier_recyclerview_row,parent,false);
        db = Room.databaseBuilder(view.getContext(), ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return new suppliersRescViewAdapter.MySupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySupViewHolder holder, int position) {

        try{
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            } else {

                try (InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(suppliers.get(position).s_image_uri))) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    int targetW = 60;
                    int targetH = 60;

                    float scaleFactor = Math.min((float) targetW / bitmap.getWidth(), (float) targetH / bitmap.getHeight());

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scaleFactor), (int) (bitmap.getHeight() * scaleFactor), true);

                    holder.supImg.setImageBitmap(resizedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException npe){

        }

        holder.supName.setText(suppliers.get(position).sname);
        holder.delSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new deleteConfirmation(holder.supName.getText().toString(),suppliesProduct,suppliers,position)).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    public static class MySupViewHolder extends RecyclerView.ViewHolder{

        ImageView supImg;
        TextView supName;
        ImageButton delSup;

        public MySupViewHolder(@NonNull View itemView) {
            super(itemView);

            supImg = itemView.findViewById(R.id.imageView3);
            supName = itemView.findViewById(R.id.supTxtView);
            delSup = itemView.findViewById(R.id.deleteSupplier);
        }
    }
}
