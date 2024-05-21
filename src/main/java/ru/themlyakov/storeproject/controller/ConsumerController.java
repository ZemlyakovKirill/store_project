package ru.themlyakov.storeproject.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.themlyakov.storeproject.entity.Consumer;
import ru.themlyakov.storeproject.service.ConsumerService;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Consumer addConsumer(@RequestBody @Valid Consumer consumer) {
        return consumerService.insert(consumer);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Consumer updateConsumer(@RequestBody @Valid Consumer consumer) {
        return consumerService.update(consumer);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Consumer deleteConsumer(@RequestParam(value = "id") Long id) {
        return consumerService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Consumer> selectAllConsumer() {
        return consumerService.selectAll();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Consumer getConsumerById(@RequestParam("consumer_id") Long id) {
        return consumerService.selectById(id);
    }
}
