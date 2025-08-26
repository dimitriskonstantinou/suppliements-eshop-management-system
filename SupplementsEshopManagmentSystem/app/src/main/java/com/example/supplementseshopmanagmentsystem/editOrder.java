package com.example.supplementseshopmanagmentsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class editOrder extends Fragment {

    private ProSupDB db;
    private DocumentReference firestore;
    private  List<ProductRDB> orderProducts;
    private editOrderRescViewAdapter adapter;
    private Button applyChanges;
    private RecyclerView recyclerView;
    private Button deleteOrder;

    private ArrayList<String> selectedProducts;
    private ArrayList<Long> quantityOfProducts;
    private ArrayList<Long> quantityOfProductsTemp;

    private int position;

    public editOrder() {
    }

    public editOrder(int position) {
        this.position=position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_order, container, false);

        recyclerView = view.findViewById(R.id.rescViewInEdit);
        applyChanges = view.findViewById(R.id.applychanges);
        deleteOrder = view.findViewById(R.id.deleteOrder);
        orderProducts = new ArrayList<>();
        quantityOfProductsTemp = new ArrayList<>();
        Fragment frag = this;


        firestore = FirebaseFirestore.getInstance().collection("sales").document(Integer.toString(position));
        db = Room.databaseBuilder(getContext(),ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                selectedProducts =(ArrayList<String>) documentSnapshot.get("items");
                quantityOfProducts = (ArrayList<Long>) documentSnapshot.get("quantities");
                for(int i=0; i<quantityOfProducts.size(); i++){
                    orderProducts.add(db.proSupDAO().showProductByName(selectedProducts.get(i)));
                }

                adapter = new editOrderRescViewAdapter(getContext(),orderProducts,quantityOfProducts);
                for(int i=0; i<quantityOfProducts.size(); i++){
                    quantityOfProductsTemp.add(i,orderProducts.get(i).pquantity+quantityOfProducts.get(i));
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Could not retrieve information from the database", Toast.LENGTH_LONG).show();
            }
        });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.isQuantOK()){
                    Map<String,Object> updates = new HashMap<>();
                    int totalitems=0;
                    int totalCost=0;
                    quantityOfProducts=adapter.getQuantityOfProducts();
                    orderProducts=adapter.getOrderProducts();
                    for(int i=0; i<orderProducts.size(); i++){
                        totalitems+=quantityOfProducts.get(i);
                        List<SuppliesProductRDB> suppliesProductRDBList = db.proSupDAO().getSingleProductFromSupPro(orderProducts.get(i).pname);
                        totalCost+=suppliesProductRDBList.get(0).pcost*quantityOfProducts.get(i);
                        orderProducts.get(i).pquantity = (int) (quantityOfProductsTemp.get(i)-Math.toIntExact(quantityOfProducts.get(i)));
                        db.proSupDAO().updateProductQuantity(orderProducts.get(i));
                        selectedProducts.set(i,orderProducts.get(i).pname);
                    }

                    updates.put("totalitems",totalitems);
                    updates.put("totalcost",totalCost);
                    updates.put("items",selectedProducts);
                    updates.put("quantities", quantityOfProducts);

                    firestore.update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getParentFragmentManager().beginTransaction().remove(frag);
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new sales()).addToBackStack(null).commit();
                                    Toast.makeText(getContext(), "Order has been updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }else{
                    Toast.makeText(getContext(), "Quantity is not enough for some products. Check for warnings during typing the quantity", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.fragment_container, new deleteConfirmation(position)).addToBackStack(null).commit();
            }
        });

        return view;
    }
}