package ru.themlyakov.storeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.themlyakov.storeproject.entity.Consumer;
import ru.themlyakov.storeproject.repository.ConsumerRepository;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.List;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;

    public Consumer insert(Consumer consumer) {
        return consumerRepository.insert(consumer);
    }

    public Consumer update(Consumer consumer) {
        if (consumer.getId() == null) {
            throw new DataManipulationException("Id is null");
        }
        return consumerRepository.update(consumer);
    }

    public Consumer delete(Long id) {
        return consumerRepository.delete(id);
    }

    public List<Consumer> selectAll() {
        return consumerRepository.selectAll();
    }

    public Consumer selectById(Long id) {
        return consumerRepository.selectById(id);
    }
}
