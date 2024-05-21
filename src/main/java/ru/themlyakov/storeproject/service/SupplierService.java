package ru.themlyakov.storeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.themlyakov.storeproject.entity.Supplier;
import ru.themlyakov.storeproject.repository.SupplierRepository;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier insert(Supplier supplier) {
        return supplierRepository.insert(supplier);
    }

    public Supplier update(Supplier supplier) {
        if (supplier.getId() == null) {
            throw new DataManipulationException("Id is null");
        }
        return supplierRepository.update(supplier);
    }

    public Supplier delete(Long id) {
        return supplierRepository.delete(id);
    }

    public List<Supplier> selectAll() {
        return supplierRepository.selectAll();
    }

    public Supplier selectById(Long id) {
        return supplierRepository.selectById(id);
    }
}
