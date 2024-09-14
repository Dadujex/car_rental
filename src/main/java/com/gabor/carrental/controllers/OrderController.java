package com.gabor.carrental.controllers;

import com.gabor.carrental.data.CarEntity;
import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.models.OrderModel;
import com.gabor.carrental.models.SearchModel;
import com.gabor.carrental.services.CarService;
import com.gabor.carrental.services.OrderService;
import com.gabor.carrental.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
@SessionAttributes("dateRange")
public class OrderController {

    OrderService orderService;
    CarService carService;

    @Autowired
    public void SetOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void SetCarService(CarService carService){
        this.carService = carService;
    }

    @GetMapping
    private String selectDate(@ModelAttribute("message") String message, Model model) {

        model.addAttribute("dateRangeSearchModel", new SearchModel());
        model.addAttribute("message", message);

        return "search";
    }

    @PostMapping
    private RedirectView loginFailure(RedirectAttributes attributes) {

        attributes.addFlashAttribute("message", "Failed to log in");

        return new RedirectView("/");
    }

    @PostMapping("/saveDateRange")
    private String saveDateRange(@ModelAttribute("dateRangeSearchModel") SearchModel searchModel, Model model) {

        model.addAttribute("dateRange", searchModel.getDateRange());

        return "redirect:/cars";
    }

    @GetMapping("/cars")
    private String listCars(@SessionAttribute(value = "dateRange", required = false) String dateRange, Model model) {

        if (dateRange == null){
            return "redirect:/";
        }

        List<Date> dates = DateUtils.dateRangeToDates(dateRange);

        List<CarEntity> cars = orderService.findBetweenDates(dates.get(0), dates.get(1));

        model.addAttribute("cars", cars);

        return "order/searchResults";
    }

    @PostMapping("/car/{id}")
    private String selectCar(@PathVariable int id) {

        return "redirect:/car/" + id;
    }

    @GetMapping("/car/{id}")
    private String showCarReservationForm(@SessionAttribute("dateRange") String dateRange,
                                          @PathVariable int id, Model model) {

        CarModel car = carService.findById(id);

        if(car == null){
            return "redirect:/";
        }

        OrderModel order = new OrderModel();
        order.setNumberOfDays(DateUtils.dayBetweenDates(dateRange));
        List<Date> dates = DateUtils.dateRangeToDates(dateRange);
        order.setStartDate(dates.get(0));
        order.setEndDate(dates.get(1));
        order.setTotalPrice(car.getPrice() * order.getNumberOfDays());
        order.setCarId(car.getId());

        model.addAttribute("car", car);
        model.addAttribute("order", order);

        return "order/orderCar";
    }


    @PostMapping("/submitOrder")
    private String submitOrder(@ModelAttribute("order") OrderModel order) {

        orderService.save(order);

        return "redirect:/";
    }

}
