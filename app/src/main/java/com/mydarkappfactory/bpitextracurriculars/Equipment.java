package com.mydarkappfactory.bpitextracurriculars;

import java.util.ArrayList;


public class Equipment {

    private int quantity;
    private ArrayList<String> issuedBy;
    private ArrayList<String> requestedBy;

    public Equipment(int quantity, ArrayList<String> issuedBy, ArrayList<String> requestedBy) {
        this.quantity = quantity;
        this.issuedBy = issuedBy;
        this.requestedBy = requestedBy;
    }

    public ArrayList<String> getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(ArrayList<String> requestedBy) {
        this.requestedBy = requestedBy;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(ArrayList<String> issuedBy) {
        this.issuedBy = issuedBy;
    }
}
