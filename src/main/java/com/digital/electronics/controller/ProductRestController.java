package com.digital.electronics.controller;

import com.digital.electronics.exceptions.ProductListNotFoundException;
import com.digital.electronics.exceptions.ProductNotFoundException;
import com.digital.electronics.model.Product;
import com.digital.electronics.repo.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    ProductRestController(ProductRepository productRepository){

        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Product>> getProducts(){
        logger.info("---Getting all products---");
        List<Product> products = this.productRepository.findAll();

        if(products.isEmpty()){
            logger.error("---Did not find any product---");
            throw new ProductListNotFoundException();
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody Product input, UriComponentsBuilder ucBuilder){
        logger.debug("---Adding new product '" + input.productName +"'---");
        Product result = this.productRepository.save(
                new Product(
                        input.productName,
                        input.productCode,
                        input.price,
                        input.releaseDate,
                        input.description,
                        input.starRating,
                        input.imageUrl,
                        input.getStock()
                ));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/products/{id}").buildAndExpand(result.getId()).toUri());
        return new ResponseEntity<>(result, headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{productId}")
    ResponseEntity<?> readProduct(@PathVariable String productId){
        logger.debug("---Reading product '" + productId +"'---");
        Product product = this.productRepository.findOne(productId);

        if(product == null){
            logger.error("---Unable to read product'" + productId +"' not found---");
            throw new ProductNotFoundException(productId);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{productId}")
    ResponseEntity<?> updateProduct(@RequestBody Product input, @PathVariable String productId){
        logger.info("---Updating product '" + productId +"'---");
        Product product = this.productRepository.findOne(productId);

        if(product == null){
            logger.error("---Unable to update product'" + productId +"' not found---");
            throw new ProductNotFoundException(productId);
        }

        Product updated = this.productRepository.save(input);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{productId}")
    ResponseEntity<?> delete(@PathVariable String productId){
        if(this.productRepository.exists(productId)){
            logger.debug("---Deleting product '" + productId +"'---");
            this.productRepository.delete(productId);
            return ResponseEntity.ok("Product '" + productId + "' deleted.");
        }
        else {
            throw new ProductNotFoundException(productId);
        }
    }
}
