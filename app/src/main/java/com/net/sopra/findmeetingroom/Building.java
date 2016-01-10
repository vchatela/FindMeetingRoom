package com.net.sopra.findmeetingroom;

public class Building {
    private int ID;
    private int idLocation;
    private String nom;

    public Building(int ID, String nom, int idLocation) {
        this.ID = ID;
        this.nom = nom;
        this.idLocation = idLocation;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) { this.ID = ID; }

    public int getLocation() {
        return idLocation;
    }

    public void setLocation(int idLocation) {
        this.idLocation = idLocation;
    }

    public String getnom() {
        return nom;
    }

    public void setnom(String nom) {
        this.nom = nom;
    }
}
