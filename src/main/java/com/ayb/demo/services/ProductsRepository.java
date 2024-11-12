package com.ayb.demo.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayb.demo.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}


