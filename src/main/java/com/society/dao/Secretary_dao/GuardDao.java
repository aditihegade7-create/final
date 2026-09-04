package com.society.dao.Secretary_dao;

import java.util.List;

import com.society.model.Secretary_model.Guard;

public interface GuardDao {

    // Add Guard
    boolean addGuard(Guard guard);

    // All Guards
    List<Guard> getAllGuards();

    // Guards of specific Society
    List<Guard> getGuardsBySociety(String society);

    // Update Guard
    boolean updateGuard(
            String id,
            String shift,
            String status,
            String assignedGate
    );

    // Guard by Email
    Guard getGuardByEmail(String email);
}