package ru.themlyakov.storeproject.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.themlyakov.storeproject.entity.Supplier;
import ru.themlyakov.storeproject.service.SupplierService;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier addSupplier(@RequestBody @Valid Supplier supplier) {
        return supplierService.insert(supplier);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier updateSupplier(@RequestBody @Valid Supplier supplier) {
        return supplierService.update(supplier);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Supplier deleteSupplier(@RequestParam(value = "id") Long id) {
        return supplierService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Supplier> selectAllSupplier() {
        return supplierService.selectAll();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Supplier getSupplierById(@RequestParam("id") Long id) {
        return supplierService.selectById(id);
    }
}
