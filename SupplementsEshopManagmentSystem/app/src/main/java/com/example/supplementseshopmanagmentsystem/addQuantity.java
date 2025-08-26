package com.example.supplementseshopmanagmentsystem;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManagerNonConfig;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addQuantity extends Fragment {

    private ProductRDB productRDB;

    public addQuantity(ProductRDB productRDB) {
        this.productRDB = productRDB;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_quantity, container, false);
        ProSupDB db = Room.databaseBuilder(getContext(),ProSupDB.class,"pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        Button order = view.findViewById(R.id.orderProduct);
        Fragment frag = this;
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = view.findViewById(R.id.quantityEditText);
                productRDB.pquantity += Integer.parseInt(editText.getText().toString());
                db.proSupDAO().updateProductQuantity(productRDB);
                getParentFragmentManager().beginTransaction().remove(frag).commit();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new productsFrag(),"productsFrag").addToBackStack(null).commit();
                Toast.makeText(getContext(), "Order completed succesfully", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}