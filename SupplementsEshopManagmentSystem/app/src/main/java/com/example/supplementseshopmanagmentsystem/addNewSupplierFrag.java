package com.example.supplementseshopmanagmentsystem;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class addNewSupplierFrag extends Fragment {

    private EditText supName;
    private EditText supPhone;
    private Button addNewSupBtn;
    private String getSupName;
    private String getSupPhone;
    private ImageButton supplierImageButton;
    private List<SupplierRDB> suppliers;
    private ProSupDB db;
    private Uri uri;
    private LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_supplier, container, false);
        supName = view.findViewById(R.id.supplierName);
        supPhone = view.findViewById(R.id.supPhone);
        addNewSupBtn = view.findViewById(R.id.addNewSupplier);
        layout = view.findViewById(R.id.supplierImageContainer);
        supplierImageButton = view.findViewById(R.id.supplierImageButton);
        db = Room.databaseBuilder(getContext(),ProSupDB.class,"pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        suppliers = db.proSupDAO().getAllSuppliers();
        addNewSupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSupplierName()&&checkSupplierPhone()){
                    try{
                        db.proSupDAO().upsertSupplier(new SupplierRDB(suppliers.size()+1,getSupName,getSupPhone,uri.toString()));

                    }catch(SQLiteConstraintException sql){
                        Toast.makeText(v.getContext(), "Supplier already exists", Toast.LENGTH_LONG).show();
                    }catch(NullPointerException npe){
                        db.proSupDAO().upsertSupplier(new SupplierRDB(suppliers.size()+1,getSupName,getSupPhone,null));
                    } finally{
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new manageSuppliers(),"manageSuppliers").addToBackStack(null).commit();
                        Toast.makeText(getContext(), "Supplier added successfully", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        supplierImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            TextView imagePath = new TextView(getContext());
            imagePath.setText(uri.getPath());
            layout.addView(imagePath);
        }
    }

    public boolean checkSupplierName(){

        getSupName = supName.getText().toString();

        if(getSupName.isEmpty()){
            supName.setError("Supplier's name can not be empty");
            return false;
        }

        supName.setError(null);
        return true;
    }

    public boolean checkSupplierPhone(){

        getSupPhone = supPhone.getText().toString();

        if(getSupPhone.isEmpty()){
            supPhone.setError("Supplier's phone number can not be empty");
            return false;
        }

        supPhone.setError(null);
        return true;
    }

}