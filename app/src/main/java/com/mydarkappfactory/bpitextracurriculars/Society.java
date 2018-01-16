package com.mydarkappfactory.bpitextracurriculars;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Society {
    public static String NO_CATEGORY = "NO_CATEGORY";
    public static class SPORTS {
        public static String BG = "BG";
        public static String B = "B";
        public static String BGSD = "BGSD";
        public static String BwtGwt = "BwtGwt";
    }
    public static class SPORTS_CATEGORIES {
        public static final String SINGLES = "Singles"
                , DOUBLES = "Doubles"
                , FIFTYFIVE_TO_SIXTYFIVE = "55-65KG"
                , SIXTYFIVE_TO_SEVENTYFIVE = "65-75KG"
                , SEVENTYFIVE_PLUS = "75+KG"
                , SIXTY_MINUS = "60-KG"
                , SIXTY_PLUS = "60+KG"
                , TEAM_MEMBER = "Team Member";
    }
    public static class SPORTS_GENDERS {
        public static final String MALE = "Male"
                , FEMALE = "Female";
    }
    public static String TECH = "TECH";
    public static String CULT = "CULT";
    private String name, description;
    private ArrayList<String> coordinators;
    private String category;
    private int imgId, members, fees;
    public static final String LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

    public Society(String name, String description, int imgId, String category, String ...coordinators) {
        this.name = name;
        this.description = description;
        this.imgId = imgId;
        this.members = 0;
        this.fees = 0;
        this.category = category;
        this.coordinators = new ArrayList<>();
        Collections.addAll(this.coordinators, coordinators);
    }

    public Society(String json) {
        Gson gson = new Gson();
        this.name = gson.fromJson(json, Society.class).getName();
        this.description = gson.fromJson(json, Society.class).getDescription();
        this.imgId = gson.fromJson(json, Society.class).getImgId();
        this.members = gson.fromJson(json, Society.class).getMembers();
        this.fees = gson.fromJson(json, Society.class).getFees();
        this.category = gson.fromJson(json, Society.class).getCategory();
        this.coordinators = gson.fromJson(json, Society.class).getCoordinators();
    }

    public ArrayList<String> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(ArrayList<String> coordinators) {
        this.coordinators = coordinators;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
