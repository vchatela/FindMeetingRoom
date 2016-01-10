package com.net.sopra.findmeetingroom;

public class Location {
    private int ID;
    private String nom;

    public Location(int ID, String nom) {
        this.nom = nom ;
        this.ID = ID ;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) { this.ID = id; }

    public String getnom() {
        return nom;
    }

    public void setnom(String name) {
        this.nom = name;
    }
}