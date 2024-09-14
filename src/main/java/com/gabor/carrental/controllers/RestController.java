package com.gabor.carrental.controllers;

import com.gabor.carrental.data.CarEntity;
import com.gabor.carrental.data.OrderEntity;
import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.models.OrderModel;
import com.gabor.carrental.services.CarService;
import com.gabor.carrental.services.OrderService;
import com.gabor.carrental.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final CarService carService;
    private final OrderService orderService;

    @Autowired
    public RestController(CarService carService, OrderService orderService) {
        this.carService = carService;
        this.orderService = orderService;
    }

    @GetMapping("/cars/{start}->{end}")
    public ResponseEntity<List<CarEntity>> getAllCars(@PathVariable String start,
                                                     @PathVariable String end) {

        if (start.compareTo(end) > 0) {
            return ResponseEntity.notFound().build();
        }

        List<Date> dates;

        try{
            dates = DateUtils.dateRangeToDates(start + "->" + end);
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }

        List<CarEntity> res = carService.findBetweenDates(dates.get(0), dates.get(1));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable int id) {

        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping("/orders")
    public ResponseEntity<String> rentCar(@RequestBody OrderModel order, UriComponentsBuilder ucb) {

        List<OrderEntity> orders =
                orderService.findByCarIdWithDates(order.getCarId(), order.getStartDate(), order.getEndDate());

        if(!orders.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        OrderEntity savedOrder = orderService.save(order);

        URI locationOfNewOrder = ucb
                .path("orders/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewOrder).build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CarModel>> findAll() {
        List<CarModel> res = carService.findAll();

        return ResponseEntity.ok(res);
    }
}
