package com.example.supplementseshopmanagmentsystem;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class sales extends Fragment {

    private CardView addbtn;
    private ArrayList<User> userArrayList;
    private RecyclerView recyclerView;
    private salesAdapter adapter;
    private FirebaseFirestore db;

    private int counterForIdAfterDelete;

    public sales(){};

    public sales(int counterForIdAfterDelete){
        this.counterForIdAfterDelete = counterForIdAfterDelete;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        recyclerView = view.findViewById(R.id.everysalerecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list ){
                    User obj = d.toObject(User.class);
                    obj.setOrderid(d.getId());
                    userArrayList.add(obj);
                }
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new salesAdapter(getContext(),userArrayList);
        recyclerView.setAdapter(adapter);

        addbtn = view.findViewById(R.id.salesCardView);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_addsale(counterForIdAfterDelete)).addToBackStack(null).commit();
            }
        });


        return view;
    }



}