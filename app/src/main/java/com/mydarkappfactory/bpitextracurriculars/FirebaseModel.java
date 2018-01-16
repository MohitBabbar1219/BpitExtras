package com.mydarkappfactory.bpitextracurriculars;

/**
 * Created by dragonslayer on 19/11/17.
 */

public class FirebaseModel {

    public static class Societies {
        public static String SOCIETIES = "SOCIETIES",
                SPORTS = "SPORTS",
                TECHNICAL = "TECHNICAL",
                CULTURAL = "CULTURAL",
                SUBSCRIBED_USERS = "SUBSCRIBED_USERS";
        public static String REASON = "REASON";
    }

    public static class Users {
        public static String USERS = "USERS"
                , IS_FIRST_LOGIN = "firstLogin"
                , HAS_REQUESTED_ITEM = "hasRequestedItem"
                , IS_ITEM_REQUEST_ACCEPTED = "isItemRequestAccepted"
                , REQUESTED_ITEM = "requestedItem"
                , EVENTS_REGISTERED = "eventsRegistered";
    }

    public static class Equipments {
        public static String EQUIPMENTS = "EQUIPMENTS"
                , SPORTS = "SPORTS"
                , FOOTBALL = "FOOTBALL"
                , BASKETBALL = "BASKETBALL"
                , VOLLEYBALL = "VOLLEYBALL"
                , QUANTITY = "quantity"
                , ISSUED_BY = "issuedBy"
                , REQUESTED_BY = "requestedBy";
    }

    public static class SPORTS_MEET {
        public static final String SPORTS_MEET_TABLE = "Sports Meet",
                REGISTRATIONS = "Registrations",
                EVENTS = "Events",
                SCHEDULE = "Schedule";
    }

}
