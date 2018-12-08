package com.alex.kotlin.myapplication;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 871136882801008L;
    String name;
    int age;

    public Book(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
