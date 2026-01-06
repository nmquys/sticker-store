package com.nmquys.springbootstore.service.impl;

import com.nmquys.springbootstore.dto.ProductDto;
import com.nmquys.springbootstore.entity.Product;
import com.nmquys.springbootstore.repository.ProductRepository;
import com.nmquys.springbootstore.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService
{

    private final ProductRepository productRepository;

    @Cacheable("products")
    @Override
    public List<ProductDto> getProducts()
    {
        return productRepository.findAll()
                .stream().map(this::transformToDTO).collect(Collectors.toList());
    }

    private ProductDto transformToDTO(Product product)
    {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        productDto.setProductId(product.getId());
        return productDto;
    }
}
