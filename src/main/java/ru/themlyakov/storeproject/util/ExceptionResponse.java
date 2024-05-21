package ru.themlyakov.storeproject.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Data
public class ExceptionResponse {

    private final List<String> errors = new ArrayList<>();

    public ExceptionResponse(String... reasons){
        Collections.addAll(errors, reasons);
    }
    public void addError(String errorMessage){
        errors.add(errorMessage);
    }
}
