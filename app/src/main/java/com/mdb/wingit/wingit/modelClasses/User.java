package com.mdb.wingit.wingit.modelClasses;

import java.util.ArrayList;

/**
 * User's name, email, and their past adventures
 */

//FIXME I don't know exact context, but you can store name, email and even profile picture in the FirebaseUser object
public class User {
    private String email;
    private String name;
    private ArrayList<String> adventureKeysList;

    public User() {

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
        if (adventureKeysList == null) {
            adventureKeysList = new ArrayList<>();
        }
        adventureKeysList.add(key);
    }

    @Override
    public String toString() {
        if (adventureKeysList != null) {
            return email + " " + name + " " + adventureKeysList.toString();
        }

        return email + " " + name;

    }
}
