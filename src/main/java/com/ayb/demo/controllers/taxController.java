package com.ayb.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayb.demo.enums.taxEnum;
import com.ayb.demo.models.Product;
import com.ayb.demo.services.LogService;
import com.ayb.demo.services.ProductsRepository;
import com.ayb.demo.services.UserRepository;


@RestController
@Controller
@RequestMapping("/tax")

public class taxController {
    @Autowired 
    private UserRepository userRepo;

    @Autowired
    private ProductsRepository productRepo;

    @Autowired
    private LogService logService;

    
      @PostMapping("/calculateWithProductId/{id}") 
    public ResponseEntity<Object> calculateTax(
        Authentication auth,
        @PathVariable int id
        ) {
        try { 
            if (!productRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            Product product = productRepo.findById(id).get();
            
            if (!product.getUserEmail().equals(auth.getName())) {

                logService.log("Unauthorized tax calculation", "TAX ERROR", auth.getName(), null,String.valueOf(id));

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to calculate tax for this product");
            }

            var userCountry = userRepo.findByEmail(auth.getName()).getCountry();
            var taxRateOfCountry = taxEnum.valueOf(userCountry).getRate();

            double tax = (product.getPrice() * taxRateOfCountry )/100;
            logService.log("Tax of product calculated", "TAX", auth.getName(), String.format("%.2f",tax),String.valueOf(product.getId()));

            return ResponseEntity.ok(tax);
        } catch (Exception e) {
            logService.log(e.getMessage(), "TAX ERROR", auth.getName(), null,String.valueOf(id));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tax could not be calculated.");
        }
    }
}
