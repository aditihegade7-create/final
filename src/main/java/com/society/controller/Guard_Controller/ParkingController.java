package com.society.controller.Guard_Controller;

import java.util.List;

import com.society.dao.Guard_dao.ParkingDao;
import com.society.model.Guard_model.ParkingModel;

public class ParkingController {

    private final ParkingDao parkingDao;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ParkingController() {
        parkingDao = new ParkingDao();
    }

    // =====================================================
    // SAVE / PARK VEHICLE
    // =====================================================

    public boolean saveParking(
            String email,
            String society,
            String spotNumber,
            String vehicleNumber,
            String ownerName,
            String vehicleType) {

        ParkingModel parking = new ParkingModel();

        parking.setSpotNumber(
                spotNumber == null
                        ? null
                        : spotNumber.trim()
        );

        parking.setVehicleNumber(
                vehicleNumber == null
                        ? null
                        : vehicleNumber.trim().toUpperCase()
        );

        parking.setOwnerName(
                ownerName == null
                        ? null
                        : ownerName.trim()
        );

        parking.setVehicleType(
                vehicleType == null
                        ? null
                        : vehicleType.trim()
        );

        parking.setOccupied(true);

        parking.setEmail(
                email == null
                        ? null
                        : email.trim().toLowerCase()
        );

        parking.setSociety(
                society == null
                        ? null
                        : society.trim()
        );

        return parkingDao.saveParking(
                parking,
                society
        );
    }

    // =====================================================
    // GET ALL PARKING FOR GUARD
    // =====================================================

    public List<ParkingModel> getAllParking(
            String email) {

        return parkingDao.getAllParking(email);
    }

    // =====================================================
    // GET PARKING BY SPOT FOR GUARD
    // =====================================================

    public ParkingModel getParkingBySpot(
            String spotNumber,
            String email) {

        return parkingDao.getParkingBySpot(
                spotNumber,
                email
        );
    }

    // =====================================================
    // RELEASE PARKING
    // =====================================================

    public boolean releaseParking(
            String parkingId) {

        return parkingDao.releaseParking(
                parkingId
        );
    }
}