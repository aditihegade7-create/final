package com.society.dao.Secretary_dao;

import java.util.List;

import com.society.model.Secretary_model.Owner;

public interface OwnerDao {

    // =========================================================
    // ADD OWNER
    // =========================================================

    boolean addOwner(Owner owner);

    // =========================================================
    // GET ALL OWNERS
    // Used by Secretary
    // =========================================================

    List<Owner> getAllOwners();

    // =========================================================
    // GET OWNERS BY SOCIETY
    // Used when owners of a particular society are required
    // =========================================================

    List<Owner> getOwnersBySociety(String society);

    // =========================================================
    // GET OWNER BY EMAIL
    // =========================================================

    Owner getOwnerByEmail(String email);

    // =========================================================
    // UPDATE OWNER
    // =========================================================

    boolean updateOwner(
            String email,
            String name,
            String flat,
            String mobile,
            String status,
            String society
    );

    // =========================================================
    // DELETE OWNER
    // =========================================================

    boolean deleteOwner(String email);
}