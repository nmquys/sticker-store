package com.nmquys.springbootstore.repository;

import com.nmquys.springbootstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long>
{}