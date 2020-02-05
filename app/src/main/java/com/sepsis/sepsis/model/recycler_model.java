package com.sepsis.sepsis.model;

public class recycler_model {
    private  String  username, name, doc;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public recycler_model(String username, String name, String doc) {
        this.username = username;
        this.name = name;
        this.doc = doc;
    }

    public recycler_model() {
    }
}
