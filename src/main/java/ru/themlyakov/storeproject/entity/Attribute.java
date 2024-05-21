package ru.themlyakov.storeproject.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Attribute {
    @Id
    @Min(value = 1, message = "Id must be greeter than 0")
    private Long id;
    @NotNull(message = "key must be not null")
    private String key;
    private String value;
    private transient int level;
    private transient Long parentId;
    private transient Integer productId;
    private final Set<Attribute> subAttribute = new HashSet<>();
}
