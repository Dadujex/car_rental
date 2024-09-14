package com.gabor.carrental.services;

import com.gabor.carrental.data.CarEntity;
import com.gabor.carrental.data.OrderDAO;
import com.gabor.carrental.data.OrderEntity;
import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.models.OrderModel;
import com.gabor.carrental.util.DateUtils;
import com.gabor.carrental.util.ModelMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    private final CarService carService;
    private final OrderDAO orderDAO;

    @Autowired
    public OrderService(CarService carService, OrderDAO orderDAO) {
        this.carService = carService;
        this.orderDAO = orderDAO;
    }

    public List<OrderEntity> findAll(){

        return orderDAO.findAllByOrderByIdAsc();
    }

    public OrderEntity findById(int id){

        return orderDAO.findById(id).orElse(null);
    }

    public List<OrderEntity> findByCarIdWithDates(int carId, Date start, Date end){

        return orderDAO.findByCarIdWithDates(carId, start, end);
    }

    public List<CarEntity> findBetweenDates(Date startDate, Date endDate){

        return carService.findBetweenDates(startDate, endDate);
    }

    @Transactional
    public OrderEntity save(OrderModel orderModel) {

        OrderEntity order = ModelMapper.modelToEntity(orderModel);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if(order.getStartDate() == null || order.getEndDate() == null){
            return null;
        }

        order.setNumberOfDays(DateUtils.dayBetweenDates(formatter.format(order.getStartDate()) + "->" +
                formatter.format(order.getEndDate())));

        CarModel car = carService.findById(orderModel.getCarId());
        if(car == null){
            return null;
        }

        order.setTotalPrice(car.getPrice() * order.getNumberOfDays());
        order.setCar(ModelMapper.modelToEntity(car));

        return orderDAO.save(order);
    }

    @Transactional
    public void deleteByCarId(int id){
        orderDAO.deleteByCarId(id);
    }

}
