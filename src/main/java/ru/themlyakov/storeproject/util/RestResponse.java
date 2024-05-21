package ru.themlyakov.storeproject.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestResponse {
    private int status;

    private Object response;
}
