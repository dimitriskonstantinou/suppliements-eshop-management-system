package com.example.supplementseshopmanagmentsystem;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;

public class deleteConfirmation extends Fragment {

    private BlurView blurView;
    private Button confirmDelete;
    private Button declineDelete;
    private TextView delMes;
    private String delname;
    private ProSupDB db;
    private List<SuppliesProductRDB> suppliesProduct;
    private List<SupplierRDB> suppliers;
    private List<ProductRDB> productRDB;
    private int position;
    private int sizeForIdAfterDelete;

    public deleteConfirmation(){}

    public deleteConfirmation(int position){this.position=position;}
    public deleteConfirmation(String delname, List<SuppliesProductRDB> suppliesProduct, List<SupplierRDB> suppliers, int position) {
        this.delname=delname;
        this.suppliesProduct=suppliesProduct;
        this.suppliers=suppliers;
        this.position=position;
    }

    public deleteConfirmation(String delname, List<ProductRDB> productRDB, int position) {
        this.delname=delname;
        this.productRDB=productRDB;
        this.position=position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delete_confirmation, container, false);

        blurView =  view.findViewById(R.id.blurView);
        confirmDelete = view.findViewById(R.id.confirmDelete);
        declineDelete = view.findViewById(R.id.declineDelete);
        db = Room.databaseBuilder(view.getContext(), ProSupDB.class, "pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        delMes = view.findViewById(R.id.delMes);
        if(delname!=null){
            delMes.setText(delMes.getText().toString()+" "+delname);
        }else{
            delMes.setText(delMes.getText().toString()+" this order?");
        }
        Fragment frag = this;

        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getPreviousFragment().equals(getParentFragmentManager().findFragmentByTag("manageSuppliers"))){
                    removeProductsIfSupDeleted(suppliesProduct,suppliers.get(position));
                    db.proSupDAO().deleteSupplier(suppliers.get(position));
                    suppliers.remove(position);
                    getParentFragmentManager().beginTransaction().remove(frag).commit();
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new manageSuppliers(),"manageSuppliers").addToBackStack(null).commit();
                    Toast.makeText(getContext(), "Supplier deleted successfully", Toast.LENGTH_LONG).show();
                } else if (getPreviousFragment().equals(getParentFragmentManager().findFragmentByTag("productsFrag"))) {
                    db.proSupDAO().deleteProduct(productRDB.get(position));
                    productRDB.remove(position);
                    getParentFragmentManager().beginTransaction().remove(frag).commit();
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new productsFrag(),"productsFrag").addToBackStack(null).commit();
                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_LONG).show();
                } else if (getPreviousFragment().equals(getParentFragmentManager().findFragmentByTag("editOrder"))) {
                    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("sales");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            sizeForIdAfterDelete = task.getResult().size();
                            DocumentReference docRef = collectionReference.document(Integer.toString(position));

                           docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                              @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {
                                  List<Long> quantityOfProducts = (ArrayList<Long>) documentSnapshot.get("quantities");
                                  List<String> selectedProducts =(ArrayList<String>) documentSnapshot.get("items");
                                  List<ProductRDB> productsToChange = new ArrayList<>();

                                  for(int i=0; i<selectedProducts.size(); i++){
                                      productsToChange.add(db.proSupDAO().showProductByName(selectedProducts.get(i)));
                                      productsToChange.get(i).pquantity+=Math.toIntExact(quantityOfProducts.get(i));
                                      db.proSupDAO().updateProductQuantity(productsToChange.get(i));
                                 }

                                  docRef.delete()
                                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {
                                                  getParentFragmentManager().beginTransaction().remove(frag).commit();
                                                  getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new sales(sizeForIdAfterDelete),"productsFrag").addToBackStack(null).commit();
                                                  Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_LONG).show();
                                              }
                                          })
                                          .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {

                                              }
                                          });
                               }
                           });



                        }
                    });

                }
            }
        });

        declineDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().remove(frag).commit();
            }
        });


        blurBackground();

        return view;
    }

    public void blurBackground(){

        float radius = 15f;
        Window window = getActivity().getWindow();
        View decorView = window.getDecorView();

        ViewGroup rootView = decorView.findViewById(android.R.id.content);

        Drawable windowsBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowsBackground)
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true);
    }

    public Fragment getPreviousFragment(){
        int previousFrag = getParentFragmentManager().getFragments().size()-2;
        Fragment fragment = getParentFragmentManager().getFragments().get(previousFrag);
        return fragment;
    }

    public void removeProductsIfSupDeleted(List<SuppliesProductRDB> suppliesProduct, SupplierRDB supplierRDB){
        String sname = supplierRDB.sname;

        for(int i=0; i<suppliesProduct.size(); i++){
            if(suppliesProduct.get(i).supName.equals(sname)){
                db.proSupDAO().deleteProductByPrimary(suppliesProduct.get(i).proName);
                db.proSupDAO().deleteSuppliesProduct(suppliesProduct.get(i));
            }
        }
    }
}