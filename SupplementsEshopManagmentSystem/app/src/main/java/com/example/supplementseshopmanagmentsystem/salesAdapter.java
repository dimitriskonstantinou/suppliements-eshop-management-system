package com.example.supplementseshopmanagmentsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class salesAdapter extends RecyclerView.Adapter<salesAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<User> userArrayList;

    public salesAdapter() {}


    public salesAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public salesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.everysale_recyclerview_row,parent,false);
        return new salesAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull salesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.orderid.setText(userArrayList.get(position).getOrderid());
        holder.username.setText(userArrayList.get(position).getUsername());
        holder.totalitems.setText(Integer.toString((userArrayList.get(position).getTotalitems())));
        holder.totalcost.setText(Double.toString(userArrayList.get(position).getTotalcost())+"$");
        holder.editOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new editOrder(Integer.parseInt(userArrayList.get(position).getOrderid())),"editOrder").addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView orderid;

        TextView totalitems;

        TextView totalcost;

        TextView username;

        ImageButton editOrder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderid = itemView.findViewById(R.id.orderid);
            username = itemView.findViewById(R.id.username);
            totalitems = itemView.findViewById(R.id.firstname);
            totalcost = itemView.findViewById(R.id.secondname);
            editOrder = itemView.findViewById(R.id.editOrderPen);
        }
    }
}
