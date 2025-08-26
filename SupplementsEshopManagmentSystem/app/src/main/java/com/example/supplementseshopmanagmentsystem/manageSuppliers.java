package com.example.supplementseshopmanagmentsystem;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class manageSuppliers extends Fragment {

    private List<SupplierRDB> suppliers;

    private ProSupDB db;

    private CardView addSupplierBtn;

    List<SuppliesProductRDB> suppliesProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_suppliers, container, false);

        addSupplierBtn = view.findViewById(R.id.addSupBtn);
        db = Room.databaseBuilder(view.getContext(), ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        suppliers = db.proSupDAO().getAllSuppliers();
        suppliesProduct = db.proSupDAO().getAllSuppliesProductRDB();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.supRescView);
        suppliersRescViewAdapter adapter = new suppliersRescViewAdapter(getContext(),suppliers,suppliesProduct);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addSupplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new addNewSupplierFrag()).addToBackStack(null).commit();
            }
        });
        return view;
    }
}