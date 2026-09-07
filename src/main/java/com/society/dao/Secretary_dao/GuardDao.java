
package com.society.dao.Secretary_dao;

import java.util.List;

import com.society.model.Secretary_model.Guard;

public interface GuardDao {

    // ============================================================
    // ADD GUARD
    // ============================================================

    boolean addGuard(Guard guard);

    // ============================================================
    // GET ALL GUARDS
    // ============================================================

    List<Guard> getAllGuards();

    // ============================================================
    // GET GUARDS BY SOCIETY
    // ============================================================

    List<Guard> getGuardsBySociety(String society);

    // ============================================================
    // UPDATE GUARD
    // ============================================================

    boolean updateGuard(
            String id,
            String shift,
            String status,
            String assignedGate
    );

    // ============================================================
    // GET GUARD BY EMAIL
    // ============================================================

    Guard getGuardByEmail(String email);
}

