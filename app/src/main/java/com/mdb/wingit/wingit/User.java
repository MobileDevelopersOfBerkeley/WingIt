package com.mdb.wingit.wingit;

import java.util.ArrayList;

/**
 * Created by reddy on 11/11/2016.
 */

public class User {
    String email;
    String name;
    ArrayList<String> adventureKeysList;

    public User() {
        this.adventureKeysList = new ArrayList<>();
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.adventureKeysList = new ArrayList<>();
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getAdventureKeysList() {
        return this.adventureKeysList;
    }

    public void addAdventureKey(String key) {
        this.adventureKeysList.add(key);
    }
}
