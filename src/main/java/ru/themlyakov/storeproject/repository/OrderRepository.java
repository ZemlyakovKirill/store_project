package ru.themlyakov.storeproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.themlyakov.storeproject.entity.Consumer;
import ru.themlyakov.storeproject.entity.Product;
import ru.themlyakov.storeproject.util.DataManipulationException;

@Repository
public class OrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    private static final String MAKE_ORDER = """
            INSERT INTO public.order(consumer_id,product_id) VALUES(?,?);
            """;
    private static final String DELETE_ORDER = """
                        DELETE FROM public.order WHERE id = ?;
            """;

    private static final String DELETE_ALL_ORDERS = """
            DELETE FROM public.order WHERE consumer_id=? AND product_id=?;
            """;

    public Integer makeOrder(Long consumerId, Long productId) {
        Product product;
        Consumer consumer;
        try {
            product = productRepository.selectById(productId);
        } catch (DataManipulationException e) {
            throw new DataManipulationException("Product was not found");
        }
        try {
            consumer = consumerRepository.selectById(consumerId);

        } catch (DataManipulationException e) {
            throw new DataManipulationException("Consumer was not found");
        }
        return save(consumer, product);
    }

    private Integer save(Consumer consumer, Product product) {
        try {
            return template.update(MAKE_ORDER, consumer.getId(), product.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Cannot insert order");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Cannot insert order");
        }
    }

    public Integer deleteAllOrders(Long consumerId, Long productId) {
        try {
            productRepository.selectById(productId);
        } catch (DataManipulationException e) {
            throw new DataManipulationException("Product was not found");
        }
        try {
            consumerRepository.selectById(consumerId);

        } catch (DataManipulationException e) {
            throw new DataManipulationException("Consumer was not found");
        }
        try {
            return template.update(DELETE_ALL_ORDERS, consumerId, productId);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Cannot delete orders");
        } catch (DataAccessException e) {
            logger.info(e.getMessage());
            throw new DataManipulationException("Cannot delete orders");
        }

    }

    public Integer delete(Long orderId) {
        try {
            return template.update(DELETE_ORDER, orderId);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Cannot insert order");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Cannot insert order");
        }
    }
}
