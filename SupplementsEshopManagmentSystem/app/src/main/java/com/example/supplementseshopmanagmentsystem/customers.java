package com.example.supplementseshopmanagmentsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class customers extends Fragment {

    private ArrayList<User> userArrayList;

    private RecyclerView recyclerView;

    private FirebaseFirestore db;

    private customersAdapter adapter;
    public customers(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customers, container, false);
        recyclerView = view.findViewById(R.id.customersrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("customers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    User obj = d.toObject(User.class);
                    userArrayList.add(obj);
                }
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new customersAdapter(getContext(),userArrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}