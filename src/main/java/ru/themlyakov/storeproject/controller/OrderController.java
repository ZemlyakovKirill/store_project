package ru.themlyakov.storeproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.themlyakov.storeproject.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/make", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Integer makeOrder(@RequestParam("product_id") Long productId,
                             @RequestParam("consumer_id") Long consumerId) {
        return orderService.makeOrder(consumerId, productId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Integer deleteOrder(@RequestParam("id") Long orderId) {
        return orderService.deleteOrder(orderId);
    }

    @RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
    public Integer deleteAllOrders(@RequestParam("product_id") Long productId,
                                   @RequestParam("consumer_id") Long consumerId) {
        return orderService.deleteAllOrders(consumerId, productId);
    }
}
