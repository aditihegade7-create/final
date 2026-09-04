package com.society.controller.Guard_Controller;

import java.util.List;

import com.society.dao.Guard_dao.EmergencyDao;
import com.society.model.Guard_model.EmergencyModel;

public class EmergencyController {

    private final EmergencyDao emergencyDao;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmergencyController() {

        emergencyDao =
                new EmergencyDao();
    }


    // =====================================================
    // CREATE EMERGENCY
    // =====================================================

    public EmergencyModel createEmergency(
            String email,
            String society,
            String type,
            String location,
            String details,
            String time) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Guard email is missing."
                );

                return null;
            }


            if (society == null ||
                    society.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Society name is missing."
                );

                return null;
            }


            EmergencyModel emergency =
                    new EmergencyModel();


            emergency.setEmail(
                    email.trim()
                            .toLowerCase()
            );


            emergency.setSociety(
                    society.trim()
            );


            emergency.setType(
                    type
            );


            emergency.setLocation(
                    location
            );


            emergency.setDetails(
                    details == null
                            ? ""
                            : details
            );


            emergency.setTime(
                    time
            );


            emergency.setStatus(
                    "ACTIVE"
            );


            emergency.setCreatedAt(
                    new java.util.Date()
            );


            return emergencyDao.createEmergency(
                    emergency
            );


        } catch (Exception e) {

            System.out.println(
                    "ERROR: EmergencyController failed."
            );

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<EmergencyModel>
    getAllEmergencies() {

        return emergencyDao.getAllEmergencies();
    }


    // =====================================================
    // GET ACTIVE
    // =====================================================

    public List<EmergencyModel>
    getActiveEmergencies() {

        return emergencyDao.getActiveEmergencies();
    }


    // =====================================================
    // GET ALL EMERGENCIES BY EMAIL
    // =====================================================

    public List<EmergencyModel>
    getEmergenciesByEmail(
            String email) {

        return emergencyDao.getEmergenciesByEmail(
                email
        );
    }


    // =====================================================
    // GET ACTIVE EMERGENCIES BY EMAIL
    // =====================================================

    public List<EmergencyModel>
    getActiveEmergenciesByEmail(
            String email) {

        return emergencyDao.getActiveEmergenciesByEmail(
                email
        );
    }


    // =====================================================
    // GET BY SOCIETY
    // =====================================================

    public List<EmergencyModel>
    getEmergenciesBySociety(
            String society) {

        return emergencyDao.getEmergenciesBySociety(
                society
        );
    }


    // =====================================================
    // RESOLVE EMERGENCY
    // EMAIL + EMERGENCY ID
    // =====================================================

    public boolean resolveEmergency(
            String email,
            String emergencyId) {

        return emergencyDao.resolveEmergency(
                email,
                emergencyId
        );
    }


    // =====================================================
    // LEGACY / COMPATIBILITY RESOLVE
    //
    // This keeps existing code that currently calls:
    //
    // resolveEmergency(emergencyId)
    //
    // from breaking immediately.
    //
    // It finds the emergency first, obtains its email,
    // and then uses the new email-based resolve method.
    // =====================================================

    public boolean resolveEmergency(
            String emergencyId) {

        try {

            if (emergencyId == null ||
                    emergencyId.trim().isEmpty()) {

                return false;
            }


            List<EmergencyModel> emergencies =
                    emergencyDao.getAllEmergencies();


            for (EmergencyModel emergency :
                    emergencies) {

                if (emergency == null) {
                    continue;
                }


                if (emergencyId.equals(
                        emergency.getEmergencyId()
                )) {

                    String email =
                            emergency.getEmail();


                    if (email == null ||
                            email.trim().isEmpty()) {

                        System.out.println(
                                "ERROR: Emergency email is missing."
                        );

                        return false;
                    }


                    return emergencyDao.resolveEmergency(
                            email,
                            emergencyId
                    );
                }
            }


            System.out.println(
                    "ERROR: Emergency not found: "
                            + emergencyId
            );


            return false;


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to resolve emergency."
            );

            e.printStackTrace();

            return false;
        }
    }
}