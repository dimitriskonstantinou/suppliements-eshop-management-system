package com.example.supplementseshopmanagmentsystem;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

public class addNewProductFrag extends Fragment {

    private Spinner productTypes;
    private EditText productNames;
    private EditText prosupName;
    private EditText cost;
    private Button addProductBtn;
    private ImageButton imageButton;
    private String getName;
    private String getType;
    private String getsupProname;
    private String getCost;
    private ProSupDB db;
    private List<ProductRDB> products;
    private LinearLayout fragsLayout;
    private Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_product, container, false);
        productNames = view.findViewById(R.id.productNames);
        productTypes = view.findViewById(R.id.productTypes);
        addProductBtn = view.findViewById(R.id.addProductButton);
        prosupName = view.findViewById(R.id.supproname);
        cost = view.findViewById(R.id.cost);
        imageButton = view.findViewById(R.id.productImageButton);
        fragsLayout = view.findViewById(R.id.productImageContainer);

        db = Room.databaseBuilder(getContext(),ProSupDB.class,"pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.product_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        products = db.proSupDAO().getAllProducts();

        productTypes.setAdapter(adapter);
        productTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getType = productTypes.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkproductName() && checkproductType() && checkprosupName() && checkCost()){
                    if(!db.proSupDAO().getAllSuppliers().isEmpty()){
                        int supid=getSidFromSupplierName(getsupProname);
                        if(supid!=-1){
                            try{
                                db.proSupDAO().upsertProduct(new ProductRDB(products.size()+1,getName,getType,uri.toString()));
                            }catch(SQLiteConstraintException sql){
                                Toast.makeText(getContext(),"Product already exists", Toast.LENGTH_LONG).show();
                            }catch (NullPointerException npe){
                                db.proSupDAO().upsertProduct(new ProductRDB(products.size()+1,getName,getType,null));
                            }finally {
                                db.proSupDAO().upsertSuppliesProduct(new SuppliesProductRDB(getName,getsupProname,Double.parseDouble(getCost)));
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new productsFrag(),"productsFrag").addToBackStack(null).commit();
                                Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(v.getContext(), "Supplier's name cannot be found in the system", Toast.LENGTH_LONG).show();
                        }
                        
                    }else{
                        Toast.makeText(v.getContext(), "There are not registered suppliers to the system", Toast.LENGTH_LONG).show();
                    }
                    
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            TextView imagePath = new TextView(getContext());
            imagePath.setText(uri.getPath());
            fragsLayout.addView(imagePath);
        }
    }

    public boolean checkproductName(){

         getName = productNames.getText().toString();

         if(getName.isEmpty()){
             productNames.setError("Product name can not be empty");
                return false;
            }

         productNames.setError(null);
         return true;
    }

    public boolean checkprosupName(){

        getsupProname = prosupName.getText().toString();

        if(getsupProname.isEmpty()){
            prosupName.setError("Supplier's name can not be empty");
            return false;
        }

        prosupName.setError(null);
        return true;
    }

    public boolean checkCost(){

        getCost = cost.getText().toString();

        if(getCost.isEmpty()){
            cost.setError("Cost field can not be empty");
            return false;
        }

        cost.setError(null);
        return true;
    }

    public boolean checkproductType(){
        if(productTypes.getSelectedItem().toString().equals("")){
            Toast.makeText(getContext(), "Please select a product type", Toast.LENGTH_LONG).show();
        }
        return true;
    }


    public int getSidFromSupplierName(String name){
        List<SupplierRDB> suppliers = db.proSupDAO().getAllSuppliers();
        for(int i=0; i<suppliers.size(); i++){
            if(suppliers.get(i).sname.equals(name)){
                return suppliers.get(i).sid;
            }
        }
        return -1;
    }
}