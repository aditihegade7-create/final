package com.society.controller.Owner_Controller;

import com.society.dao.Owner_dao.PaymentDao;
import com.society.model.Owner_model.PaymentModel;

import java.util.List;

public class PaymentController {

    // =========================================================
    // DAO
    // =========================================================

    private final PaymentDao paymentDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PaymentController() {

        paymentDao =
                new PaymentDao();
    }


    // =========================================================
    // ADD PAYMENT
    // =========================================================

    public boolean addPayment(
            PaymentModel payment) {

        try {

            if (payment == null) {

                return false;
            }


            if (payment.getOwnerEmail() == null ||
                    payment.getOwnerEmail()
                            .trim()
                            .isEmpty()) {

                return false;
            }


            return paymentDao.addPayment(
                    payment
            );


        } catch (Exception e) {

            System.out.println(
                    "PaymentController Add Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET OWNER PAYMENTS
    // =========================================================

    public List<PaymentModel> getOwnerPayments(
            String ownerEmail) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return List.of();
            }


            return paymentDao.getOwnerPayments(
                    ownerEmail
                            .trim()
                            .toLowerCase()
            );


        } catch (Exception e) {

            System.out.println(
                    "PaymentController Get Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return List.of();
        }
    }
}