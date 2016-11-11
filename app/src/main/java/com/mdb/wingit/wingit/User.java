package com.mdb.wingit.wingit;

import java.util.ArrayList;

/**
 * Created by reddy on 11/11/2016.
 */

public class User {
    String email;
    String name;
    ArrayList<String> adventureKeyList;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.adventureKeyList = new ArrayList<>();
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getAdventureKeyList() {
        return this.adventureKeyList;
    }

    public void addAdventureKey(String key) {
        this.adventureKeyList.add(key);
    }
}
