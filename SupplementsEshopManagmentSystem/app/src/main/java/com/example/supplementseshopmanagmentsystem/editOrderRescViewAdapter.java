package com.example.supplementseshopmanagmentsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

public class editOrderRescViewAdapter extends RecyclerView.Adapter<editOrderRescViewAdapter.editOrderViewHolder> {

    private Context context;
    private List<ProductRDB> orderProducts;
    private ArrayList<Long> quantityOfProducts;
    private ProSupDB db;

    private boolean isQuantOK=true;

    public boolean isQuantOK() {
        return isQuantOK;
    }

    public List<ProductRDB> getOrderProducts() {
        return orderProducts;
    }

    public ArrayList<Long> getQuantityOfProducts() {
        return quantityOfProducts;
    }

    public editOrderRescViewAdapter(Context context, List<ProductRDB> orderProducts, ArrayList<Long> quantityOfProducts) {
        this.context = context;
        this.orderProducts = orderProducts;
        this.quantityOfProducts = quantityOfProducts;
    }

    @NonNull
    @Override
    public editOrderRescViewAdapter.editOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.products_in_editorder_row, parent, false);
        db = Room.databaseBuilder(context,ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return new editOrderRescViewAdapter.editOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull editOrderRescViewAdapter.editOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(orderProducts.get(position).pname);

        try{
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            } else {

                try (InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(orderProducts.get(position).image_uri))) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    int targetW = 100;
                    int targetH = 100;

                    float scaleFactor = Math.min((float) targetW / bitmap.getWidth(), (float) targetH / bitmap.getHeight());

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scaleFactor), (int) (bitmap.getHeight() * scaleFactor), true);

                    holder.proImage.setImageBitmap(resizedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException npe){

        }

        holder.quantityEdt.setText(quantityOfProducts.get(position).toString());
        holder.quantityEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(Long.parseLong(holder.quantityEdt.getText().toString())>db.proSupDAO().showProductByName(orderProducts.get(position).pname).pquantity){
                        throw new NotEnoughQuantityInStockException();
                    }
                    quantityOfProducts.set(position,Long.parseLong(holder.quantityEdt.getText().toString()));
                }catch(NumberFormatException e){

                }catch(NotEnoughQuantityInStockException neqise){
                    isQuantOK=false;
                    Toast.makeText(context, "Not enough quantity in stock for product: "+orderProducts.get(position).pname, Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.delProFromOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderProducts.remove(position);
                quantityOfProducts.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderProducts.size();
    }

    public static class editOrderViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        EditText quantityEdt;
        TextView quantitytxt;
        ImageView proImage;
        ImageButton delProFromOrder;

        public editOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rescPname);
            quantityEdt = itemView.findViewById(R.id.quantInEdit);
            quantitytxt = itemView.findViewById(R.id.rescQuantityInEdit);
            proImage = itemView.findViewById(R.id.rescImage);
            delProFromOrder = itemView.findViewById(R.id.deleteProductInEdit);
        }
    }
}
