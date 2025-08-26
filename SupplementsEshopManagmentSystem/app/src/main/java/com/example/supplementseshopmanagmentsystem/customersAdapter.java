package com.example.supplementseshopmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customersAdapter extends RecyclerView.Adapter<customersAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<User> userArrayList;

    public customersAdapter(){}

    public customersAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public customersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customers_recyclerview_row,parent,false);
        return new customersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customersAdapter.MyViewHolder holder, int position) {
        holder.username.setText(userArrayList.get(position).getUsername());
        holder.firstname.setText(userArrayList.get(position).getFirstname());
        holder.lastname.setText(userArrayList.get(position).getLastname());
        holder.email.setText(userArrayList.get(position).getEmail());
        holder.phonenumber.setText(userArrayList.get(position).getPhonenumber());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username;

        TextView firstname;

        TextView lastname;

        TextView email;

        TextView phonenumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.uname2);
            firstname = itemView.findViewById(R.id.fname2);
            lastname = itemView.findViewById(R.id.lname2);
            email = itemView.findViewById(R.id.emailtxt2);
            phonenumber = itemView.findViewById(R.id.phonetxt2);
        }
    }
}
