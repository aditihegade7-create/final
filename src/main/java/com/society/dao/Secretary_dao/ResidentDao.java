package com.society.dao.Secretary_dao;

import java.util.List;

import com.society.model.Secretary_model.Resident;

public interface ResidentDao {

    // =========================================================
    // ADD / UPDATE RESIDENT
    // =========================================================

    boolean addResident(Resident resident);

    // =========================================================
    // GET ALL RESIDENTS
    // =========================================================

    List<Resident> getAllResidents();

    // =========================================================
    // GET RESIDENT BY EMAIL
    // =========================================================

    Resident getResidentByEmail(String email);

    // =========================================================
    // GET RESIDENTS BY SOCIETY
    // =========================================================

    List<Resident> getResidentsBySociety(String society);
}