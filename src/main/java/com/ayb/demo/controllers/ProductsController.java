package com.ayb.demo.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ayb.demo.models.Product;
import com.ayb.demo.models.ProductDto;
import com.ayb.demo.services.ProductsRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.ayb.demo.services.LogService;



@Controller
@RequestMapping("/products")

public class ProductsController {


    @Autowired 
    private ProductsRepository repo;

    @Autowired
    private LogService logService;


    @GetMapping({"", "/"})
public ResponseEntity<Map<String, Object>> showProductList() {
    List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));


    Map<String, Object> response = new HashMap<String, Object>();
    response.put("message", "İŞLEM BAŞARILI, TÜM ÜRÜNLER:");
    response.put("products", products);

    return new ResponseEntity<>(response, HttpStatus.OK);
}

    @GetMapping("byId/{id}") 
    public ResponseEntity <?> getProductById (
    Authentication auth,
    @PathVariable int id,
        Model model
    ) {
        try{
            Product product = repo.findById(id).get();
            if (product.equals(null)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            if (!product.getUserEmail().equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to view this product");
            }
            return ResponseEntity.ok(product);
        }
        catch(Exception e) {
            System.out.println("Exception Of Get by Id:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ürün bulunamadı.");
        }
        
    }



    @PostMapping("/save")
    public ResponseEntity<?> saveProductPostman(Authentication auth,@Valid @RequestBody ProductDto request) {
        try { 
            Date createdAt = new Date();
            
            Product product = new Product();
            product.setName(request.getName());
            product.setBrand(request.getBrand());
            product.setCategory(request.getCategory());
            product.setCreatedAt(createdAt);
            product.setPrice(request.getPrice());
            product.setDescription(request.getDescription());
            product.setUserEmail(auth.getName());
                        
    
            Product savedProduct = repo.save(product);
    
            logService.log("New product added" , "NEW PRODUCT", auth.getName(),null,String.valueOf(savedProduct.getId()));
            return ResponseEntity.ok(savedProduct);
    
        } catch (Exception e) {
            logService.log(e.getMessage() , "NEW PRODUCT FAIL", auth.getName(),null,null);

            System.out.println("Exception of add new product: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ürün kaydedilemedi.");
        }
    }
    

    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> updateProductById(
        Authentication auth,
        @PathVariable int id, @RequestBody ProductDto request) {
        try {
            Product updateProduct = repo.findById(id).get();
            if (updateProduct.equals(null)) {

                System.out.println("Update Product Not Found");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Update product not found");
            }
            if (!updateProduct.getUserEmail().equals(auth.getName())) {
                System.out.println("Update Product Not Found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this product");
            }


        if (request.getName() != null) {
            updateProduct.setName(request.getName());
        }
        if (request.getBrand() != null) {
            updateProduct.setBrand(request.getBrand());
        }
        if (request.getCategory() != null) {
            updateProduct.setCategory(request.getCategory());
        }

        if (request.getDescription() != null) {
            updateProduct.setDescription(request.getDescription());
        }

            repo.save(updateProduct);
            logService.log(" Product edited" , "PATCH PRODUCT", auth.getName(),null,String.valueOf(updateProduct.getId()));

            return ResponseEntity.ok(updateProduct);
           
            
        }catch(Exception e) {
            System.out.println("Exception Of Update: " + e);

            logService.log(e.getMessage() , "PATCH PRODUCT FAIL", auth.getName(),null,String.valueOf(id));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during update");
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteProductById(
        Authentication auth,
        @PathVariable int id) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            if (!repo.findById(id).get().getUserEmail().equals(auth.getName())) {

                logService.log("Unauthorized product deletion" , "DELETE PRODUCT FAIL", auth.getName(),null,String.valueOf(id));

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this product");
            }
            repo.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body("Deletion successful");
        }catch (Exception e) {
            
            System.out.println("Exception Of Delete: " + e.getMessage());

            logService.log(e.getMessage() , "DELETE PRODUCT FAIL", auth.getName(),null,String.valueOf(id));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during deletion");
        }
    }
 
}
