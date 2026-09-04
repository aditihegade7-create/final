package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Event;
import com.society.model.Welcome.User;

public class EventDao {

    private final Firestore firestore;

    private static final String COLLECTION_NAME = "Events";

    public EventDao() {
        firestore = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // GET LOGGED-IN SECRETARY
    // =========================================================

    private User getLoggedInSecretary() {

        UserDao userDao = new UserDao();

        User secretary = userDao.getLoggedInSecretary();

        if (secretary == null) {
            System.out.println("Logged-in secretary not found.");
        }

        return secretary;
    }

    // =========================================================
    // ADD EVENT
    // =========================================================

    public boolean addEvent(Event event) {

        try {

            if (event == null) {
                System.out.println("Event is null.");
                return false;
            }

            // -------------------------------------------------
            // GET LOGGED-IN SECRETARY
            // -------------------------------------------------

            User secretary = getLoggedInSecretary();

            if (secretary == null) {
                return false;
            }

            // -------------------------------------------------
            // GET EMAIL
            // -------------------------------------------------

            String email = secretary.getEmail();

            if (email == null || email.trim().isEmpty()) {

                System.out.println(
                        "Secretary email not found."
                );

                return false;
            }

            email = email.trim().toLowerCase();

            // -------------------------------------------------
            // GET SOCIETY
            // -------------------------------------------------

            String society = secretary.getSociety();

            if (society == null || society.trim().isEmpty()) {

                System.out.println(
                        "Secretary society not found."
                );

                return false;
            }

            society = society.trim();

            // -------------------------------------------------
            // EVENT DATA
            // -------------------------------------------------

            Map<String, Object> eventData =
                    new HashMap<>();

            eventData.put(
                    "eventName",
                    event.getEventName()
            );

            eventData.put(
                    "date",
                    event.getDate()
            );

            eventData.put(
                    "time",
                    event.getTime()
            );

            eventData.put(
                    "venue",
                    event.getVenue()
            );

            eventData.put(
                    "status",
                    event.getStatus()
            );

            eventData.put(
                    "email",
                    email
            );

            eventData.put(
                    "society",
                    society
            );

            eventData.put(
                    "timestamp",
                    System.currentTimeMillis()
            );

            // -------------------------------------------------
            // IMPORTANT
            // -------------------------------------------------
            // EMAIL = PARENT DOCUMENT ID
            //
            // Events
            //    └── secretary@gmail.com
            //          └── events
            //                └── auto event id
            //
            // -------------------------------------------------

            firestore
                    .collection(COLLECTION_NAME)
                    .document(email)
                    .collection("events")
                    .add(eventData)
                    .get();

            System.out.println(
                    "Event added successfully."
            );

            System.out.println(
                    "Email : " + email
            );

            System.out.println(
                    "Society : " + society
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while adding event."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL EVENTS
    // =========================================================

    public List<Event> getAllEvents() {

        List<Event> eventList =
                new ArrayList<>();

        try {

            // -------------------------------------------------
            // GET LOGGED-IN SECRETARY
            // -------------------------------------------------

            User secretary =
                    getLoggedInSecretary();

            if (secretary == null) {
                return eventList;
            }

            // -------------------------------------------------
            // GET EMAIL
            // -------------------------------------------------

            String email =
                    secretary.getEmail();

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Secretary email not found."
                );

                return eventList;
            }

            email =
                    email.trim().toLowerCase();

            // -------------------------------------------------
            // FETCH EVENTS USING EMAIL
            // -------------------------------------------------

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection("events")
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            // -------------------------------------------------
            // CONVERT TO EVENT OBJECT
            // -------------------------------------------------

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Event event =
                        document.toObject(Event.class);

                if (event != null) {

                    eventList.add(event);
                }
            }

            System.out.println(
                    "Events fetched for : " + email
            );

            System.out.println(
                    "Total events : "
                            + eventList.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while fetching events."
            );

            e.printStackTrace();
        }

        return eventList;
    }
}