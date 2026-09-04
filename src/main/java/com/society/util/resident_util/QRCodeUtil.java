package com.society.util.resident_util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import com.society.config.CloudinaryConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {

    private QRCodeUtil() {
    }

    // =====================================================
    // GENERATE QR CODE LOCALLY
    // =====================================================

    public static Path generateQRCode(
            String data,
            String fileName
    ) throws Exception {

        int width = 400;
        int height = 400;

        Map<EncodeHintType, Object> hints =
                new HashMap<>();

        hints.put(
                EncodeHintType.MARGIN,
                2
        );

        BitMatrix matrix =
                new MultiFormatWriter().encode(
                        data,
                        BarcodeFormat.QR_CODE,
                        width,
                        height,
                        hints
                );

        // =================================================
        // LOCAL QR FOLDER
        // =================================================

        Path qrFolder =
                Path.of(
                        System.getProperty("user.home"),
                        "Society360",
                        "QR"
                );

        Files.createDirectories(
                qrFolder
        );

        Path qrFile =
                qrFolder.resolve(fileName);

        // =================================================
        // WRITE PNG
        // =================================================

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                qrFile
        );

        return qrFile;
    }

    // =====================================================
    // UPLOAD QR IMAGE TO CLOUDINARY
    // =====================================================

    public static String uploadQRCodeToCloudinary(
            Path qrPath,
            String visitorId
    ) throws Exception {

        if (qrPath == null) {

            throw new Exception(
                    "QR file path is missing."
            );
        }

        if (!Files.exists(qrPath)) {

            throw new Exception(
                    "QR image file does not exist:\n"
                            + qrPath
            );
        }

        if (visitorId == null ||
                visitorId.trim().isEmpty()) {

            throw new Exception(
                    "Visitor ID is missing."
            );
        }

        // =================================================
        // GET CLOUDINARY INSTANCE
        // =================================================

        Cloudinary cloudinary =
                CloudinaryConfig.getCloudinary();

        // =================================================
        // UPLOAD
        // =================================================

        Map<?, ?> uploadResult =
                cloudinary
                        .uploader()
                        .upload(
                                qrPath.toFile(),
                                ObjectUtils.asMap(
                                        "resource_type",
                                        "image",

                                        "folder",
                                        "Society360/Visitors/QR",

                                        "public_id",
                                        visitorId,

                                        "overwrite",
                                        true
                                )
                        );

        // =================================================
        // GET SECURE URL
        // =================================================

        Object secureUrl =
                uploadResult.get(
                        "secure_url"
                );

        if (secureUrl == null) {

            throw new Exception(
                    "Cloudinary upload completed but "
                            + "secure URL was not returned."
            );
        }

        return secureUrl.toString();
    }
}