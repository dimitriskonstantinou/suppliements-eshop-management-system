package com.example.supplementseshopmanagmentsystem;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class ProductRDB {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String pname;

    @ColumnInfo(name = "pid")
    public int pid;

    @ColumnInfo(name = "p_type")
    public String ptype;

    @ColumnInfo(name = "p_quantity")
    public int pquantity;

    @ColumnInfo(name = "image_uri")
    public String image_uri;


    public ProductRDB(int pid,String pname, String ptype, String image_uri) {
        this.pid=pid;
        this.pname = pname;
        this.ptype = ptype;
        this.pquantity=0;
        this.image_uri = image_uri;
    }

}
