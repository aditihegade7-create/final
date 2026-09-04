package com.society.controller.Owner_Controller;

import java.util.HashMap;
import java.util.Map;

import com.society.dao.Owner_dao.OwnerDocumentDao;

public class OwnerDocumentController {

    private final OwnerDocumentDao ownerDocumentDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public OwnerDocumentController() {

        ownerDocumentDao =
                new OwnerDocumentDao();
    }


    // =========================================================
    // GET OWNER DOCUMENTS
    // =========================================================

    public Map<String, String> getOwnerDocuments(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return new HashMap<>();
            }


            return ownerDocumentDao
                    .getOwnerDocuments(
                            email.trim().toLowerCase()
                    );

        } catch (Exception e) {

            System.out.println(
                    "OwnerDocumentController Get Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return new HashMap<>();
        }
    }


    // =========================================================
    // SAVE OWNER DOCUMENTS
    // =========================================================

    public boolean saveOwnerDocuments(
            String email,
            String aadhaarUrl,
            String panUrl,
            String ownershipProofUrl,
            String addressProofUrl) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            return ownerDocumentDao
                    .saveOwnerDocuments(
                            email.trim().toLowerCase(),
                            aadhaarUrl,
                            panUrl,
                            ownershipProofUrl,
                            addressProofUrl
                    );

        } catch (Exception e) {

            System.out.println(
                    "OwnerDocumentController Save Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

}
