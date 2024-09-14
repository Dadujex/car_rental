package com.gabor.carrental.util;

import com.gabor.carrental.data.CarEntity;
import com.gabor.carrental.data.OrderEntity;
import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.models.OrderModel;

public class ModelMapper {

    public static CarModel entityToModel(CarEntity car) {
        return new CarModel(car.getId(), car.getName(), car.isActive(), car.getPrice(), car.getImage());
    }

    public static CarEntity modelToEntity(CarModel car) {
        return new CarEntity(car.getId(), car.getName(), car.isActive(), car.getPrice(), car.getImage());
    }

    public static OrderEntity modelToEntity(OrderModel order) {
        return new OrderEntity(order.getId(), order.getName(), order.getEmail(), order.getAddress(), order.getPhone(),
                order.getNumberOfDays(), order.getTotalPrice(), order.getStartDate(), order.getEndDate(), null);
    }
}
