package com.alex.kotlin.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    String name;
    float price;
    int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Product(String name, float price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeInt(this.number);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.name = in.readString();
        this.price = in.readFloat();
        this.number = in.readInt();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
