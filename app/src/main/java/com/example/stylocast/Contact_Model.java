package com.example.stylocast;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Contact_Model implements Serializable{
    String name,number;
    Bitmap imgVal;
    public Contact_Model(Bitmap imgVal,String name,String number){
        this.name= name;
        this.number = number;
        this.imgVal = imgVal;
    }
    public Contact_Model(String name, String number){
        this.name = name;
        this.number = number;
    }


}
