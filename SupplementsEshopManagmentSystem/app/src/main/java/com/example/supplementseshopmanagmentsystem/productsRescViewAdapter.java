package com.example.supplementseshopmanagmentsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class productsRescViewAdapter extends RecyclerView.Adapter<productsRescViewAdapter.myViewHolder> {
    private Context context;
    private List<ProductRDB> productRDB;
    private ProSupDB db;
    private int clicks=0;

    public void setProductRDB(List<ProductRDB> productRDB) {
        this.productRDB = productRDB;
    }

    public productsRescViewAdapter(Context context, List<ProductRDB> productRDB){
        this.context = context;
        this.productRDB = productRDB;
    }
    @NonNull
    @Override
    public productsRescViewAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_recyclerview_row,parent,false);
        db = Room.databaseBuilder(context,ProSupDB.class,"pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return new productsRescViewAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productsRescViewAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try{
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            } else {

                try (InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(productRDB.get(position).image_uri))) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    int targetW = 100;
                    int targetH = 100;

                    float scaleFactor = Math.min((float) targetW / bitmap.getWidth(), (float) targetH / bitmap.getHeight());

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scaleFactor), (int) (bitmap.getHeight() * scaleFactor), true);

                    holder.imageView.setImageBitmap(resizedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException npe){

        }

        holder.proTxt.setText(productRDB.get(position).pname);
        holder.typeTxt.setText("Type: "+productRDB.get(position).ptype);
        holder.quantTxt.setText("Quantity: "+productRDB.get(position).pquantity);
        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new deleteConfirmation(holder.proTxt.getText().toString(),productRDB,position)).addToBackStack(null).commit();
            }
        });
        holder.addQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getClicksCount(v)%2!=0){
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new addQuantity(productRDB.get(position))).addToBackStack(null).commit();
                    productRDB = db.proSupDAO().getAllProducts();
                    holder.quantTxt.setText("Quantity :"+Integer.toString(productRDB.get(position).pquantity));
                } else {
                    ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
                    clicks=0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productRDB.size();
    }

    public int getClicksCount(@NonNull View v){

        if (v.isPressed()){
            clicks++;
        }

        return clicks;
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView proTxt;
        TextView typeTxt;
        TextView quantTxt;
        ImageButton addQuantityBtn;
        ImageButton deleteProduct;
        ConstraintLayout addQuantLayout;
        public myViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.rescImage);
            proTxt = itemView.findViewById(R.id.rescPname);
            typeTxt = itemView.findViewById(R.id.rescPtype);
            quantTxt = itemView.findViewById(R.id.rescQuantity);
            deleteProduct = itemView.findViewById(R.id.deleteProduct);
            addQuantityBtn = itemView.findViewById(R.id.addQuantity);
            addQuantLayout = itemView.findViewById(R.id.addQuantLayout);
        }
    }
}
