package com.nmquys.springbootstore.service;

import com.nmquys.springbootstore.dto.ProductDto;

import java.util.List;

public interface IProductService
{
    List<ProductDto> getProducts();
}
