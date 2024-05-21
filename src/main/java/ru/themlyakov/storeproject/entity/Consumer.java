package ru.themlyakov.storeproject.entity;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Consumer {
    @Id
    private Long id;

    @NotNull(message = "First Name must be not null")
    @Size(min = 1, max = 100, message = "First Name size must be between 1 and 100")
    private String firstName;
    @NotNull(message = "Last Name must be not null")
    @Size(min = 1, max = 200, message = "Last Name size must be between 1 and 200")
    private String lastName;
    @Size(min = 1, max = 100, message = "Middle Name size must be between 1 and 250")
    private String middleName;
    @NotNull(message = "Address must be not null")
    @Size(min = 1, message = "Address size must be greeter than 1")
    private String address;
    @NotNull(message = "Contact Info must be not null")
    @Size(min = 1, message = "Contact Info size must be greeter than 1")
    private String contactInfo;
}
