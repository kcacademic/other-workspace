package com.kchandrakant.learning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kchandrakant.learning.service.VehicleService;

import java.util.List;

@Controller
public class VehicleController {

    @Value("${vehicle.registrations}")
    private List<String> vehicles;

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/vehicles")
    public String showVehicleList(Model model) {
        model.addAttribute("vehicles", vehicles);
        return "index";
    }

    @GetMapping("/vehicles/{registration}")
    public String showVehicleDetails(@PathVariable("registration") String registration, Model model) {
        model.addAttribute("details", vehicleService.getVehicleDetails(registration));
        return "details";
    }


}
