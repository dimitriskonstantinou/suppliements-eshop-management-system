package com.example.supplementseshopmanagmentsystem;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.type.TimeOfDayOrBuilder;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class productsFrag extends Fragment{

    private CardView addBtnCard;
    private List<ProductRDB> productsList;
    private ProSupDB db;
    private  productsRescViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);

        db = Room.databaseBuilder(getContext(), ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        productsList = db.proSupDAO().getAllProducts();

        adapter = new productsRescViewAdapter(getContext(),productsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AutoCompleteTextView searchView = view.findViewById(R.id.autoCompleteTextView);
        ArrayList<String> searchViewItems = new ArrayList<>();
        for(int i=0; i<productsList.size(); i++){
            searchViewItems.add(productsList.get(i).pname);
        }
        ArrayAdapter<String> searchAddapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,searchViewItems);
        searchView.setAdapter(searchAddapter);

        ImageButton searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!searchView.getText().toString().isEmpty()){
                   // TODO: 12/5/2023 FIX IMAGE SEARCH AFTER IMAGE SEARCH
                   String result = searchView.getText().toString();
                   adapter.setProductRDB(db.proSupDAO().showSingleProductByName(result));
                   adapter.notifyDataSetChanged();
               }else{
                   getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new productsFrag(), "productsFrag").addToBackStack(null).commit();
               }
            }
        });

        addBtnCard = view.findViewById(R.id.addBtnCard);
        addBtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new addNewProductFrag(),"addNewProductFrag").addToBackStack(null).commit();
            }
        });

        return view;
    }

}