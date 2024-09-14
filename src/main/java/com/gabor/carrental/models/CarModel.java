package com.gabor.carrental.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {

    private int id;
    private String name;
    private boolean active;
    private double price;
    private String image;
    private MultipartFile imageFile;

    public CarModel(int id, String name, boolean active, double price, String image) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.price = price;
        this.image = image;
    }

    public CarModel(String name, boolean active, double price, String image) {
        this.name = name;
        this.active = active;
        this.price = price;
        this.image = image;
    }

    public CarModel(CarModel carModel) {
        this.id = carModel.getId();
        this.name = carModel.getName();
        this.active = carModel.isActive();
        this.price = carModel.getPrice();
        this.image = carModel.getImage();
        this.imageFile = carModel.getImageFile();
    }
}
