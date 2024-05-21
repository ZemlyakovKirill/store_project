package ru.themlyakov.storeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.themlyakov.storeproject.repository.SupplyRepository;
import ru.themlyakov.storeproject.util.DataManipulationException;

@Service
public class SupplyService {
    @Autowired
    private SupplyRepository repository;


    @Transactional
    public Integer makeSupply(Long supplierId, Long productId, Integer quantity) {
        if (quantity <= 0) throw new DataManipulationException("quantity must be greeter than 0");
        return repository.makeSupply(supplierId, productId, quantity);
    }

    @Transactional
    public Integer deleteSupply(Long supplyId) {
        return repository.delete(supplyId);
    }

    @Transactional
    public Integer deleteAllSupplies(Long supllierId, Long productId) {
        return repository.deleteAllSupplies(supllierId, productId);
    }
}
