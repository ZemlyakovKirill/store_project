package ru.themlyakov.storeproject.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.themlyakov.storeproject.entity.Product;
import ru.themlyakov.storeproject.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody @Valid Product product) {
        return productService.insert(product);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Product updateProduct(@RequestBody @Valid Product product) {
        return productService.update(product);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Product deleteProduct(@RequestParam(value = "id") Long id) {
        return productService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Product> selectAllProduct() {
        return productService.selectAll();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Product getProductById(@RequestParam("id") Long id) {
        return productService.selectById(id);
    }

}
