package ru.themlyakov.storeproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.themlyakov.storeproject.service.SupplyService;

@RestController
@RequestMapping("/supply")
public class SupplyController {

    @Autowired
    private SupplyService supplyService;

    @RequestMapping(value = "/make", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Integer makeSupply(@RequestParam("product_id") Long productId,
                              @RequestParam("supplier_id") Long supplierId,
                              @RequestParam("quantity") Integer quantity) {
        return supplyService.makeSupply(supplierId, productId, quantity);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Integer deleteSupply(@RequestParam("id") Long supplyId) {
        return supplyService.deleteSupply(supplyId);
    }

    @RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
    public Integer deleteAllSupplies(@RequestParam("product_id") Long productId,
                                     @RequestParam("supplier_id") Long consumerId) {
        return supplyService.deleteAllSupplies(consumerId, productId);
    }
}
