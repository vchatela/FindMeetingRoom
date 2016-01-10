package com.net.sopra.findmeetingroom;

public class Specification {
    private int ID;
    private String SpecificationName;

    public Specification (int ID, String SpecificationName) {
        this.ID = ID;
        this.SpecificationName = SpecificationName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSpecificationName() {
        return SpecificationName;
    }

    public void setSpecificationName(String SpecificationName) {
        this.SpecificationName = SpecificationName;
    }

}