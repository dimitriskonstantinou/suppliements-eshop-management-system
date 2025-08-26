package com.example.supplementseshopmanagmentsystem;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class SupplierRDB {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    String sname;

    @ColumnInfo(name = "sid")
    int sid;

    @ColumnInfo(name = "s_phonenum")
    String sphonenum;

    @ColumnInfo(name = "s_image_uri")
    String s_image_uri;

    SupplierRDB(int sid,String sname, String sphonenum,String s_image_uri) {
        this.sid=sid;
        this.sname = sname;
        this.sphonenum = sphonenum;
        this.s_image_uri = s_image_uri;
    }


}
