package com.example.supplementseshopmanagmentsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class customerProductAda extends RecyclerView.Adapter<customerProductAda.myAdapter>{

    private Context context;

    private List<ProductRDB> productRDB;

    private ProSupDB psDb;

    int number1;

    private ArrayList<Integer> arrayList;

    public ArrayList<Integer> getArrayList() {
        return arrayList;
    }


    public customerProductAda(Context context, List<ProductRDB> productRDB) {
        this.context=context;
        this.productRDB=productRDB;
    }

    @NonNull
    @Override
    public customerProductAda.myAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
        View view = inflater.inflate(R.layout.sales_recyclerview_row,parent,false);
        psDb = Room.databaseBuilder(context,ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        for(int i=0; i<productRDB.size();i++){
            arrayList.add(i,0);
        }
        return new customerProductAda.myAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customerProductAda.myAdapter holder, @SuppressLint("RecyclerView") int position) {
        holder.productname.setText(productRDB.get(position).pname);
        holder.plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number1 = Integer.parseInt(holder.nmbquantity.getText().toString());
                number1++;
                holder.nmbquantity.setText(Integer.toString(number1));

                if(holder.nmbquantity.getText().toString().equals("1")){
                    arrayList.set(position,Integer.parseInt(holder.nmbquantity.getText().toString()));
                }
                else{
                    arrayList.set(position,Integer.parseInt(holder.nmbquantity.getText().toString()));
                }
            }
        });
        holder.minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.nmbquantity.getText().toString())==0){
                    Toast.makeText(context.getApplicationContext(), "You cannot remove any!",Toast.LENGTH_LONG).show();
                }
                else {
                    number1 = Integer.parseInt(holder.nmbquantity.getText().toString());
                    number1--;
                    holder.nmbquantity.setText(Integer.toString(number1));
                    arrayList.set(position,Integer.parseInt(holder.nmbquantity.getText().toString()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productRDB.size();
    }

    public static class myAdapter extends RecyclerView.ViewHolder{

        TextView productname;

        CardView plusbtn;

        CardView minusbtn;

        EditText nmbquantity;

        ImageView img;
        public myAdapter(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.salesnameproduct);
            plusbtn = itemView.findViewById(R.id.salesaddbtn);
            minusbtn = itemView.findViewById(R.id.minusbtn);
            nmbquantity = itemView.findViewById(R.id.salesquantity);
            img = itemView.findViewById(R.id.imageView2);
        }
    }

}
