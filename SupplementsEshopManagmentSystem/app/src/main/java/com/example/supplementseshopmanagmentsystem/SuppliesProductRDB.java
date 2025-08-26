package com.example.supplementseshopmanagmentsystem;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"proName","supName"},
        foreignKeys = {
        @ForeignKey(entity = ProductRDB.class,
                parentColumns = "pname",
                childColumns = "proName",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = SupplierRDB.class,
                parentColumns = "sname",
                childColumns = "supName",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)
})

public class SuppliesProductRDB {

    @NonNull
    @ColumnInfo(name = "proName")
    String proName;

    @NonNull
    @ColumnInfo(name = "supName")
    String supName;

    @ColumnInfo(name = "cost_per_item")
    double pcost;

    public SuppliesProductRDB(String proName, String supName, double pcost) {
        this.proName = proName;
        this.supName = supName;
        this.pcost = pcost;
    }
}
