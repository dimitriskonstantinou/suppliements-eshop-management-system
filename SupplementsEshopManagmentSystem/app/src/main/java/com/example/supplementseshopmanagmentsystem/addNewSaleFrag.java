package com.example.supplementseshopmanagmentsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class addNewSaleFrag extends Fragment {

    private EditText firstname;

    private EditText secondname;

    private EditText email;

    private EditText phonenumber;

    private EditText username;
    private Button btn;

    private int counter;

    private int counter2;

    private double totalcost;

    private ArrayList<String> selectedProducts;

    private ArrayList<Integer> quantityOfSelectedProducts;
    private int counterForIdAfterDelete;
    private DocumentReference documentReference;
    private DocumentReference documentReference2;

    public addNewSaleFrag() {}

    public  addNewSaleFrag(int counterForIdAfterDelete,int counter2, double totalcost, ArrayList<String> selectedProducts,ArrayList<Integer> quantityOfSelectedProducts){
        this.totalcost = totalcost;
        this.counter2 = counter2;
        this.selectedProducts = selectedProducts;
        this.quantityOfSelectedProducts = quantityOfSelectedProducts;
        this.counterForIdAfterDelete = counterForIdAfterDelete;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_sale, container, false);

        firstname = view.findViewById(R.id.firstn);
        secondname = view.findViewById(R.id.secondn);
        email = view.findViewById(R.id.emailcus);
        phonenumber = view.findViewById(R.id.phonenmb);
        username = view.findViewById(R.id.username);

        btn = view.findViewById(R.id.buttonaddsale);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUsername(username)&&checkFirstName(firstname)&&checkSecondName(secondname)&&checkEmail(email)&&checkPhone(phonenumber)){
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    CollectionReference collectionRef = firestore.collection("sales");
                    CollectionReference collectionRef2 = firestore.collection("customers");
                    collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                counter = task.getResult().size();
                                if(counter<counterForIdAfterDelete){
                                    counter = counterForIdAfterDelete;
                                }
                                if(counter!=0){
                                    if(Integer.parseInt(task.getResult().getDocuments().get(counter-1).getId())>counter){
                                        counter=Integer.parseInt(task.getResult().getDocuments().get(counter-1).getId());
                                    }
                                }
                                if(counter==0){
                                    counter = 0;
                                }
                                documentReference = firestore.collection("sales").document(Integer.toString(++counter));
                                Map<String,Object> sales = new HashMap<>();
                                sales.put("firstname", firstname.getText().toString());
                                sales.put("lastname", secondname.getText().toString());
                                sales.put("email", email.getText().toString());
                                sales.put("phonenumber", phonenumber.getText().toString());
                                sales.put("username", username.getText().toString());
                                sales.put("totalitems", counter2);
                                sales.put("totalcost",totalcost);
                                sales.put("items",selectedProducts);
                                sales.put("quantities", quantityOfSelectedProducts);
                                documentReference.set(sales).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                documentReference2 = firestore.collection("customers").document(username.getText().toString());
                                                CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("customers");
                                                documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        if (!documentSnapshot.exists()){
                                                            Map<String,Object> customers = new HashMap<>();
                                                            customers.put("firstname", firstname.getText().toString());
                                                            customers.put("lastname", secondname.getText().toString());
                                                            customers.put("username", username.getText().toString());
                                                            customers.put("email", email.getText().toString());
                                                            customers.put("phonenumber", phonenumber.getText().toString());
                                                            documentReference2.set(customers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                Toast.makeText(getContext().getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new sales()).addToBackStack(null).commit();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext().getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Log.d("notfirestore","Error getting collection size", task.getException());
                            }
                        }
                    });
                }
            }
        });
        return view;
    }

    public boolean checkFirstName(EditText name) {

        if (name.getText().toString().isEmpty()) {
            name.setError("The firstname must not be empty");
            return false;
        }

        name.setError(null);
        return true;
    }

    public boolean checkSecondName(EditText secondname) {

        if (secondname.getText().toString().isEmpty()) {
            secondname.setError("The secondname must not be empty");
            return false;
        }

        secondname.setError(null);
        return true;
    }

    public boolean checkEmail(EditText email){

        if (email.getText().toString().isEmpty()){
            email.setError("The email must not be empty");
            return false;
        }

        email.setError(null);
        return true;
    }

    public boolean checkPhone(EditText phone){

        if (phone.getText().toString().isEmpty()){
            phone.setError("The phone number must not be empty");
            return false;
        }

        phone.setError(null);
        return true;

    }

    public boolean checkUsername(EditText username){

        if (username.getText().toString().isEmpty()){
            username.setError("The username must not be empty");
            return false;
        }

        username.setError(null);
        return true;

    }

}