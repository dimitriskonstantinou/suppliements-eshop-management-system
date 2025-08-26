package com.example.supplementseshopmanagmentsystem;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProductRDB.class,SupplierRDB.class,SuppliesProductRDB.class}, version = 14)
public abstract class ProSupDB extends RoomDatabase {
    public abstract ProSupDAO proSupDAO();
}
