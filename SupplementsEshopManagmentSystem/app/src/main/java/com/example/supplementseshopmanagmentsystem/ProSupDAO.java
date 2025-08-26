package com.example.supplementseshopmanagmentsystem;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProSupDAO {


    @Query("SELECT * FROM ProductRDB")
    List<ProductRDB> getAllProducts();

    @Query("SELECT * FROM SupplierRDB")
    List<SupplierRDB> getAllSuppliers();

    @Query("SELECT * FROM SuppliesProductRDB")
    List<SuppliesProductRDB> getAllSuppliesProductRDB();

    @Query("DELETE FROM ProductRDB WHERE pname=:p_name")
    void deleteProductByPrimary(String p_name);

    @Query("SELECT * FROM ProductRDB WHERE pname=:p_name")
    List<ProductRDB> showSingleProductByName(String p_name);

    @Query("SELECT * FROM ProductRDB WHERE pname=:p_name")
    ProductRDB showProductByName(String p_name);

    @Query("SELECT * FROM SuppliesProductRDB WHERE proName=:pname")
    List<SuppliesProductRDB> getSingleProductFromSupPro(String pname);

    @Insert
    void upsertProduct(ProductRDB productRDB);

    @Update
    void updateProductQuantity(ProductRDB productRDB);

    @Insert
    void upsertSuppliesProduct(SuppliesProductRDB suppliesProductRDB);

    @Delete
    void deleteSuppliesProduct(SuppliesProductRDB suppliesProductRDB);

    @Insert
    void upsertSupplier(SupplierRDB supplierRDB);

    @Delete
    void deleteProduct(ProductRDB productRDB);

    @Delete
    void deleteSupplier(SupplierRDB supplierRDB);

}
