package com.society.controller;

import java.io.File;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.society.config.CloudinaryConfig;

public class ImageUploadController {

    public static String imageUpload(File file) {

        if (file == null) {
            return null;
        }

        Cloudinary cloudinary =
                CloudinaryConfig.getCloudinary();

        try {

            Map<String, Object> result =
                    cloudinary.uploader().upload(
                            file,
                            ObjectUtils.asMap(
                                    "resource_type",
                                    "auto"
                            )
                    );

            System.out.println(result);

            String url =
                    String.valueOf(
                            result.get("secure_url")
                    );

            System.out.println(
                    "Cloudinary URL: " + url
            );

            return url;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}