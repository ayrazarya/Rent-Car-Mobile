package com.project.sewamobil.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.project.sewamobil.util.GsonConverter;

import java.util.List;

/**
 * Entitas Mobil untuk tabel dalam database Room.
 */
@Entity(tableName = "mobil")
public class Mobil {

    @PrimaryKey(autoGenerate = true)  // Use auto-generate ID instead of 'make' as primary key
    private int id;

    private String make;  // Make of the car
    private String model; // Model of the car
    private int year;     // Year of the car
    private String color; // Color of the car
    private int mileage;  // Mileage of the car
    private int price;    // Price of the car
    private String fuelType; // Fuel type
    private String transmission; // Transmission type
    private String engine;  // Engine details
    private int horsepower; // Horsepower of the engine
    @TypeConverters(GsonConverter.class)  // Gson converter for features list
    private List<String> features;
    private int owners;   // Number of owners
    private String image; // Car image (URL or base64)

    public Mobil(String make, String model, int year, String color, int mileage, int price,
                 String fuelType, String transmission, String engine, int horsepower,
                 List<String> features, int owners, String image) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.engine = engine;
        this.horsepower = horsepower;
        this.features = features;
        this.owners = owners;
        this.image = image;
    }

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @SerializedName("car_brand")
    public String getMake() {
        return make;
    }

    public void setMake(@NonNull String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public int getOwners() {
        return owners;
    }

    public void setOwners(int owners) {
        this.owners = owners;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        // Check for null values to prevent displaying "null"
        String modelString = (model != null) ? model : "Unknown model";
        String colorString = (color != null) ? color : "Unknown color";

        return make + " " + modelString + " - $" + price;
    }




}
