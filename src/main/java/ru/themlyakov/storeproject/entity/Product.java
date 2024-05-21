package ru.themlyakov.storeproject.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class Product {
    @Id
    private Integer id;

    @NotNull(message = "Name must be not null")
    @Size(min = 1, message = "Name size must be greeter than 0")
    private String name;


    @NotNull(message = "Cost must be not null")
    @Min(value = 0 , message = "Cost value must be positive")
    private BigDecimal cost;

    private Attribute attribute;
}
