package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createdProduct(){

        Product product = new Product(1L,"Phone","Good Phone","http://img.com/img.png",800.00,Instant.parse("2020-10-30T03:00:00Z"));
        product.getCategories().add(createdCategory());
        return product;
    }

    public  static ProductDTO createdProductDTO(){
        Product product = createdProduct();
        return  new ProductDTO(product,product.getCategories());
    }

    public static Category createdCategory(){
        return  new Category(1L,"Eletronics");

    }
}
