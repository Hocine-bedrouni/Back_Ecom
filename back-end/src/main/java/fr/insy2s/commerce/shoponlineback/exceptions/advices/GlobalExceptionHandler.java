package fr.insy2s.commerce.shoponlineback.exceptions.advices;

import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.AccountNotFountException;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.OrderedNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.ProductNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFountException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFountException ex)
    {
        log.error("AccountNotFountException : ", ex);
        ErrorResponse error = new ErrorResponse("Account not found", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException productNotFoundException)
    {
        log.error("PrductNotFoundException : ", productNotFoundException);
        ErrorResponse errorResponse = new ErrorResponse("Product not found", productNotFoundException.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderedNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderedNotFoundException(OrderedNotFoundException orderedNotFoundException)
    {
        log.error("OrderedNotFoundException : ", orderedNotFoundException);

        ErrorResponse errorResponse = new ErrorResponse("Ordered not found ", orderedNotFoundException.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex)
    {
        log.error("ResponseStatusException : ", ex);
        ErrorResponse error = new ErrorResponse(ex.getReason(), ex.getMessage());

        return new ResponseEntity<>(error, ex.getStatus());
    }
}
