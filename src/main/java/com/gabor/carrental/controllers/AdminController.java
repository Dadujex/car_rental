package com.gabor.carrental.controllers;

import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.services.CarService;
import com.gabor.carrental.services.OrderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CarService carService;
    private final OrderService orderService;

    @Autowired
    public AdminController(CarService carService, OrderService orderService) {
        this.carService = carService;
        this.orderService = orderService;
    }

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @GetMapping("/login")
    public String showLoginForm(Model model) {

        model.addAttribute("username", adminUsername);
        model.addAttribute("password", adminPassword);

        return "login";
    }

    @GetMapping("/logout")
    public String logOut() {

        return "logout";
    }

    @GetMapping("/cars/add")
    public String showAddCarForm(Model model) {

        model.addAttribute("car", new CarModel());

        return "admin/addCar";
    }

    @PostMapping("/cars/add")
    public RedirectView addCar(@ModelAttribute("car") CarModel car, RedirectAttributes attributes) {

        saveImage(car);

        CarModel res = carService.save(car);
        if(res == null){
            attributes.addFlashAttribute("message", "Could not save the car");
        }

        return new RedirectView("/admin/cars");
    }

    private void saveImage(CarModel car) {

        if (car.getImageFile() != null && !car.getImageFile().isEmpty()) {
            try {
                byte[] bytes = car.getImageFile().getBytes();
                Path path = Paths.get("src/main/resources/static/images/" + car.getImageFile().getOriginalFilename());
                Files.write(path, bytes);
                car.setImage(car.getImageFile().getOriginalFilename());

            } catch (IOException e) {
                System.err.println("Something went wrong with image upload");
            }
        }
    }

    @GetMapping("/cars")
    public String showCars(@ModelAttribute("message") String message, Model model) {

        List<CarModel> cars = carService.findAll();

        model.addAttribute("cars", cars);
        model.addAttribute("message", message);

        return "admin/cars";
    }

    @PostMapping("/cars/edit")
    public String selectCar(@ModelAttribute("car") CarModel car){

        return "redirect:/admin/cars/edit/" + car.getId();
    }

    @GetMapping("/cars/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model){

        CarModel car = carService.findById(id);
        if(car == null){
            return "redirect:/admin/cars";
        }
        model.addAttribute("car", car);

        return "/admin/edit";
    }

    @PostMapping("/cars/doUpdate")
    public String updateCar(@ModelAttribute("car") CarModel car){

        if(car.getImage() == null) {
            CarModel carModel = carService.findById(car.getId());
            car.setImage(carModel.getImage());
        }

        saveImage(car);

        carService.update(car.getId(), car, car.isActive());

        return "redirect:/admin/cars";
    }

    @PostMapping("/cars/delete")
    public String deleteCar(@ModelAttribute("car") CarModel car){

        carService.deleteById(car.getId());

        return "redirect:/admin/cars";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {

        model.addAttribute("orders", orderService.findAll());


        return "admin/orders";
    }


}
