package com.society.controller.Secretary_Controller;

import java.util.List;

import com.society.dao.Secretary_dao.EventDao;
import com.society.model.Secretary_model.Event;

public class EventController {

    // =========================================================
    // DAO
    // =========================================================

    private final EventDao eventDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public EventController() {
        eventDao = new EventDao();
    }

    // =========================================================
    // ADD EVENT
    // =========================================================

    public boolean addEvent(
            String eventName,
            String date,
            String time,
            String venue,
            String status) {

        // Basic validation
        if (eventName == null || eventName.trim().isEmpty()) {
            return false;
        }

        if (date == null || date.trim().isEmpty()) {
            return false;
        }

        if (time == null || time.trim().isEmpty()) {
            return false;
        }

        if (venue == null || venue.trim().isEmpty()) {
            return false;
        }

        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        Event event = new Event(
                eventName.trim(),
                date.trim(),
                time.trim(),
                venue.trim(),
                status.trim()
        );

        return eventDao.addEvent(event);
    }

    // =========================================================
    // GET ALL EVENTS OF LOGGED-IN SECRETARY'S SOCIETY
    // =========================================================

    public List<Event> getAllEvents() {
        return eventDao.getAllEvents();
    }
}