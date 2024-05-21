package ru.themlyakov.storeproject.controller;

import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ru.themlyakov.storeproject.util.DataManipulationException;
import ru.themlyakov.storeproject.util.ExceptionResponse;
import ru.themlyakov.storeproject.util.RestResponse;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
public class ExceptionController implements ResponseBodyAdvice<Object> {


    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exceptionHandling(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exceptionNotReadable(HttpMessageNotReadableException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.addError(e.getMessage());
        return response;
    }

    @ExceptionHandler(DataManipulationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exceptionManipulation(DataManipulationException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.addError(ex.getReason());
        return response;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionResponse methodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        return new ExceptionResponse("Method '" +
                                     ex.getMethod() +
                                     "' not allowed to this path");
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse pgException(SQLException ex) throws Throwable {
        ExceptionResponse response = new ExceptionResponse();
        for (Throwable throwable : ex) {
            response.addError(throwable.getMessage());
        }
        return response;
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse servletException(ServletException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return response;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionResponse pageNotFoundException(NoHandlerFoundException ex) {
        return new ExceptionResponse("Path '" +
                                     ex.getRequestURL() +
                                     "' not found");
    }


    private ExceptionResponse processFieldErrors(List<FieldError> fieldErrors) {
        ExceptionResponse response = new ExceptionResponse();
        fieldErrors.forEach(fieldError -> response.addError(fieldError.getDefaultMessage()));
        return response;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
        int status = servletResponse.getServletResponse().getStatus();
        return new RestResponse(status, body);
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse uncheckedExceptionHandling(Throwable thr) {
        logger.error(thr.getMessage());
        thr.printStackTrace();
        return new ExceptionResponse(thr.getMessage());
    }

}
