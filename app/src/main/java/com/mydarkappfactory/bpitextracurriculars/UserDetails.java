package com.mydarkappfactory.bpitextracurriculars;

import java.util.ArrayList;
import java.util.Collections;

public class UserDetails {

    private String firstName, lastName, phoneNumber, emailAddress, password;
    private String enrollmentNumber,stream;
    private Boolean isFirstLogin;
    private Boolean hasRequestedItem;
    private Boolean isItemRequestAccepted;
    private String requestedItem;
    private ArrayList<String> eventsRegistered;

    public UserDetails() {
    }

    public UserDetails(String firstName, String lastName, String phoneNumber, String emailAddress, String password, String enrollmentNumber, String stream, Boolean isFirstLogin, Boolean hasRequestedItem, String ... eventsRegistered) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
        this.enrollmentNumber = enrollmentNumber;
        this.stream = stream;
        this.isFirstLogin = isFirstLogin;
        this.hasRequestedItem = hasRequestedItem;
        this.isItemRequestAccepted = false;
        this.requestedItem = "-1";
        this.eventsRegistered = new ArrayList<>();
        for (int i = 0; i < eventsRegistered.length; i++) {
            this.eventsRegistered.add(eventsRegistered[i]);
        }
    }

    public ArrayList<String> getEventsRegistered() {
        return eventsRegistered;
    }

    public void setEventsRegistered(ArrayList<String> eventsRegistered) {
        this.eventsRegistered = eventsRegistered;
    }

    public String getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(String requestedItem) {
        this.requestedItem = requestedItem;
    }

    public Boolean getItemRequestAccepted() {
        return isItemRequestAccepted;
    }

    public void setItemRequestAccepted(Boolean itemRequestAccepted) {
        isItemRequestAccepted = itemRequestAccepted;
    }

    public Boolean getHasRequestedItem() {
        return hasRequestedItem;
    }

    public void setHasRequestedItem(Boolean hasRequestedItem) {
        this.hasRequestedItem = hasRequestedItem;
    }

    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public Boolean getFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
