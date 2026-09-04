package com.society.controller.Guard_Controller;

import com.society.dao.Guard_dao.NoticeDao;
import com.society.model.Guard_model.NoticeModel;

public class NoticeController {

    private final NoticeDao noticeDao;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public NoticeController() {

        noticeDao =
                new NoticeDao();
    }

    // =====================================================
    // GET LATEST ACTIVE NOTICE
    // =====================================================

    public NoticeModel getLatestActiveNotice() {

        return noticeDao.getLatestActiveNotice();
    }
}