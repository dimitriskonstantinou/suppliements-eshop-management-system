package com.example.supplementseshopmanagmentsystem;
import android.content.Context;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room; import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class fragment_addsale extends Fragment{
    private CardView addBtnCard;
    private List<ProductRDB> productsList;
    private List<SuppliesProductRDB> suppliesProductRDBList;
    private ProSupDB psDb;
    private int counterForIdAfterDelete;
    private customerProductAda adapter;
    public fragment_addsale() { }
    public fragment_addsale(int counterForIdAfterDelete) {
        this.counterForIdAfterDelete = counterForIdAfterDelete;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addsale, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.salesrecyclerview);
        psDb = Room.databaseBuilder(getContext(), ProSupDB.class, "pro_sup_db" ).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        productsList = psDb.proSupDAO().getAllProducts();
        adapter = new customerProductAda(getContext(),productsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        addBtnCard = view.findViewById(R.id.btnaddsale);
        addBtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!psDb.proSupDAO().getAllProducts().isEmpty()){
                    int i =0; boolean flag=true; boolean flag2=false;
                    ArrayList<Integer> arrayList= adapter.getArrayList();
                    for(int j=0; j<arrayList.size();j++){
                        if(arrayList.get(j)!=0) {
                            flag2=true;
                        }
                    } if (flag2) {
                        while ((i < productsList.size()) && (flag == true)) {
                            if (arrayList.get(i) > productsList.get(i).pquantity) {
                                flag = false;
                            } i++;
                        } if (flag == true) {
                            int counter=0;
                            double totalCost=0;
                            String pname;
                            ArrayList<String> selectedProducts = new ArrayList<>();
                            ArrayList<Integer> quantityOfSelectedProducts = new ArrayList<>();
                            for(i=0;i<arrayList.size();i++){
                                if(arrayList.get(i)!=0){
                                    pname=productsList.get(i).pname;
                                    suppliesProductRDBList = psDb.proSupDAO().getSingleProductFromSupPro(pname);
                                    totalCost+=suppliesProductRDBList.get(0).pcost*arrayList.get(i);
                                    productsList.get(i).pquantity-=arrayList.get(i);
                                    psDb.proSupDAO().updateProductQuantity(productsList.get(i));
                                    counter+=arrayList.get(i);
                                    System.out.println(selectedProducts.size());
                                    selectedProducts.add(pname);
                                    quantityOfSelectedProducts.add(arrayList.get(i));
                                }
                            }
                            getParentFragmentManager().beginTransaction().add(R.id.fragment_container, new addNewSaleFrag(counterForIdAfterDelete,counter,totalCost,selectedProducts,quantityOfSelectedProducts), "customerinfo").addToBackStack(null).commit();
                        } else {
                            Toast.makeText(getContext(), "Not enough quantity in stock for product " + productsList.get(--i).pname, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "You didn't choose any product quantity", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "There are no products in database", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}