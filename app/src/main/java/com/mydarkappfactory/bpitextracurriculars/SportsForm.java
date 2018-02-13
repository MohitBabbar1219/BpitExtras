package com.mydarkappfactory.bpitextracurriculars;

import java.util.ArrayList;

/**
 * Created by dragonslayer on 23/12/17.
 */

public class SportsForm {
    private String gender, partnerName, partnerEnrollment;
    private String category;

    public SportsForm(String gender, String category, String partnerName, String partnerEnrollment) {
        this.gender = gender;
        this.category = category;
        this.partnerName = partnerName;
        this.partnerEnrollment = partnerEnrollment;
    }

    public SportsForm() {

    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerEnrollment() {
        return partnerEnrollment;
    }

    public void setPartnerEnrollment(String partnerEnrollment) {
        this.partnerEnrollment = partnerEnrollment;
    }
}
