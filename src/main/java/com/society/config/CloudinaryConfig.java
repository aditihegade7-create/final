package com.society.config;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;



public class CloudinaryConfig {

    public static Cloudinary cloudinary ;
    public static Cloudinary getCloudinary() {
       
        if (cloudinary == null) {

            Map<String, Object> config = new HashMap<>();

            config.put("cloud_name", "wcnbam3e");
            config.put("api_key", "269473117228257");
            config.put("api_secret", "sXtziftiB_Fdbw2kAPXwBZgDoD0");
            config.put("secure", true);


            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }
}