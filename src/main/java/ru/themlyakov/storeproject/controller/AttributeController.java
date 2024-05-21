package ru.themlyakov.storeproject.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.themlyakov.storeproject.entity.Attribute;
import ru.themlyakov.storeproject.service.AttributeService;

import java.util.List;

@RestController
@RequestMapping("/attribute")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Attribute getAllByProduct(@RequestParam("id") Long productId) {
        return attributeService.getAllByProduct(productId);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Attribute getById(@RequestParam("attribute_id") Long attributeId) {
        return attributeService.getById(attributeId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Attribute add(@RequestBody @Valid Attribute attribute, @RequestParam(value = "product_id", required = false) Long productId) {
        return attributeService.insert(productId, attribute);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Attribute update(@RequestBody @Valid Attribute attribute, @RequestParam(value = "product_id", required = false) Long productId) {
        return attributeService.update(productId, attribute);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Integer delete(@RequestParam("id") Long id) {
        return attributeService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Attribute> getAll() {
        return attributeService.selectAll();
    }
}
