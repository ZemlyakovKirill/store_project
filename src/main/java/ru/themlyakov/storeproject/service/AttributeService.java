package ru.themlyakov.storeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.themlyakov.storeproject.entity.Attribute;
import ru.themlyakov.storeproject.repository.AttributeRepository;

import java.util.List;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    public Attribute getAllByProduct(Long productId) {
        return attributeRepository.selectAllByProductId(productId);
    }

    public Attribute getById(Long attributeId) {
        return attributeRepository.selectById(attributeId);
    }

    public Attribute insert(Long productId, Attribute attribute) {
        return attributeRepository.insert(attribute, productId);
    }

    public Attribute update(Long productId, Attribute attribute) {
        return attributeRepository.update(attribute, productId);
    }

    public Integer delete(Long attributeId) {
        return attributeRepository.delete(attributeId);
    }

    public List<Attribute> selectAll() {
        return attributeRepository.selectAllAttributes(null);
    }
}
