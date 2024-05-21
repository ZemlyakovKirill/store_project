package ru.themlyakov.storeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.themlyakov.storeproject.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;


    @Transactional
    public Integer makeOrder(Long consumerId, Long productId) {
        return repository.makeOrder(consumerId, productId);
    }

    @Transactional
    public Integer deleteOrder(Long orderId) {
        return repository.delete(orderId);
    }

    @Transactional
    public Integer deleteAllOrders(Long consumerId, Long productId) {
        return repository.deleteAllOrders(consumerId, productId);
    }
}
