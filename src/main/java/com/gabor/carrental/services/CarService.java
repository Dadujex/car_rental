package com.gabor.carrental.services;

import com.gabor.carrental.data.*;
import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.util.ModelMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarService {

    private final CarDAO carDAO;
    private final OrderDAO orderDAO;

    @Autowired
    public CarService(CarDAO carDAO, OrderDAO orderDAO) {
        this.carDAO = carDAO;
        this.orderDAO = orderDAO;
    }

    public List<CarModel> findAll(){
        List<CarEntity> cars = carDAO.findAllByOrderByIdAsc();
        List<CarModel> carModels = new ArrayList<>();

        for (CarEntity car : cars){
            CarModel carModel = ModelMapper.entityToModel(car);
            carModels.add(carModel);
        }

        return carModels;
    }

    public CarModel findById(int id) {
        CarEntity entity = carDAO.findById(id).orElse(null);

        if (entity == null) return null;

        return ModelMapper.entityToModel(entity);
    }

    @Transactional
    public CarModel save(CarModel carModel) {
        CarEntity saved = carDAO.save(ModelMapper.modelToEntity(carModel));

        return ModelMapper.entityToModel(saved);
    }

    @Transactional
    public void deleteById(int id) {
        carDAO.deleteById(id);
    }

    @Transactional
    public CarModel update(int id, CarModel carModel, boolean isActive) {

        if(!isActive){
            orderDAO.deleteByCarId(id);
        }

        CarEntity updated = carDAO.save(ModelMapper.modelToEntity(carModel));

        return ModelMapper.entityToModel(updated);
    }

    public List<CarEntity> findBetweenDates(Date start, Date end) {

        if(start.compareTo(end) > 0){
            return new ArrayList<>();
        }

        return carDAO.findBetweenDates(start, end);
    }
}
