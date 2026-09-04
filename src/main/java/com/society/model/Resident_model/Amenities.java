package com.society.model.Resident_model;

public class Amenities {
    private String amenityId;
    private String amenityName;
    private String description;
    private String price;
    private String availability;

    // Runtime-only value. It is not required to exist as a Firestore field.
    private String secretaryEmail;

    public Amenities() {
    }

    public Amenities(String amenityId, String amenityName,
                     String description, String price,
                     String availability) {
        this.amenityId = amenityId;
        this.amenityName = amenityName;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }

    public Amenities(String amenityId, String amenityName,
                     String description, String price,
                     String availability, String secretaryEmail) {
        this(amenityId, amenityName, description, price, availability);
        this.secretaryEmail = secretaryEmail;
    }

    public String getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(String amenityId) {
        this.amenityId = amenityId;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSecretaryEmail() {
        return secretaryEmail;
    }

    public void setSecretaryEmail(String secretaryEmail) {
        this.secretaryEmail = secretaryEmail;
    }

    @Override
    public String toString() {
        return "Amenities{" +
                "amenityId='" + amenityId + '\'' +
                ", amenityName='" + amenityName + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", availability='" + availability + '\'' +
                ", secretaryEmail='" + secretaryEmail + '\'' +
                '}';
    }
}
