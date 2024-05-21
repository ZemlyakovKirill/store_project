package ru.themlyakov.storeproject.entity;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Supplier {
    @Id
    private Long id;

    @NotNull(message = "Name must be not null")
    @Size(min = 1, max = 100, message = "Name size must be between 1 and 100")
    private String name;
    @NotNull(message = "Address must be not null")
    @Size(min = 1, message = "Address size must be greeter than 1")
    private String address;
    @NotNull(message = "Contact Info must be not null")
    @Size(min = 1, message = "Contact Info size must be greeter than 1")
    private String contactInfo;

}
