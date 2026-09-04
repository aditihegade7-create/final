package com.society.controller.Guard_Controller;

import java.util.List;

import com.society.dao.Guard_dao.VisitorDao;
import com.society.model.Guard_model.VisitorModel;

public class VisitorController {

    private final VisitorDao visitorDao;


    public VisitorController() {

        visitorDao =
                new VisitorDao();
    }


    // =====================================================
    // GET ALL VISITORS
    // =====================================================

    public List<VisitorModel> getAllVisitors() {

        return visitorDao.getAllVisitors();
    }


    // =====================================================
    // GET VISITORS BY GUARD EMAIL
    // =====================================================

    public List<VisitorModel> getVisitorsByGuardEmail(
            String guardEmail) {

        return visitorDao.getVisitorsByGuardEmail(
                guardEmail
        );
    }


    // =====================================================
    // ADD NEW VISITOR
    // =====================================================

    public boolean addVisitor(
            VisitorModel visitor) {

        return visitorDao.addVisitor(
                visitor
        );
    }


    // =====================================================
    // ADD NEW VISITOR WITH SOCIETY
    // =====================================================

    public boolean addVisitor(
            VisitorModel visitor,
            String societyName) {

        return visitorDao.addVisitor(
                visitor,
                societyName
        );
    }


    // =====================================================
    // CHECK OUT VISITOR
    // =====================================================

    public boolean checkOutVisitor(
            String visitorId) {

        return visitorDao.checkOutVisitor(
                visitorId
        );
    }


    // =====================================================
    // ADMIT EXPECTED VISITOR
    // =====================================================

    public boolean admitVisitor(
            String visitorId,
            String guardEmail) {

        return visitorDao.admitVisitor(
                visitorId,
                guardEmail
        );
    }


    // =====================================================
    // LEGACY ADMIT METHOD
    // =====================================================

    public boolean admitVisitor(
            String visitorId) {

        return visitorDao.admitVisitor(
                visitorId
        );
    }
}