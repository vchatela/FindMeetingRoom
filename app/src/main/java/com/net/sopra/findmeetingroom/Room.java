package com.net.sopra.findmeetingroom;

public class Room {
    private int ID;
    private int idBuilding;
    private String nom;
    private int etage;

    public Room(int ID, String nom, int idBuilding, int etage) {
        this.ID = ID;
        this.nom = nom;
        this.idBuilding = idBuilding;
        this.etage = etage;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getidBuilding() {
        return idBuilding;
    }

    public void setidBuilding(int idBuilding) {
        this.idBuilding = idBuilding;
    }

    public String getnom() {
        return nom;
    }

    public void setnom(String nom) {
        this.nom = nom;
    }

    public int getetage() {
        return etage;
    }

    public void setetage(int etage) {
        this.etage = etage;
    }

}