package ru.practicum.shareit.util.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.util.exceptions.model.ErrorResponse;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    ErrorHandler handler = new ErrorHandler();

    @Test
    void handleConflictException() {
        ResourceConflictException exception = new ResourceConflictException("Conflict resource!");
        ErrorResponse thrownException = handler.handleConflictException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }

    @Test
    void handleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found!");
        ErrorResponse thrownException = handler.handleResourceNotFoundException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }

    @Test
    void testHandleResourceNotFoundException() {
        NoSuchElementException exception = new NoSuchElementException("No such element!");
        ErrorResponse thrownException = handler.handleResourceNotFoundException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }

    @Test
    void handleValidationException() {
        ValidationException exception = new ValidationException("Wrong validation!");
        ErrorResponse thrownException = handler.handleValidationException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }

    @Test
    void handleResponseStatusException() {
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse thrownException = handler.handleResponseStatusException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }

    @Test
    void handleItemHeaderException() {
        ItemHeaderException exception = new ItemHeaderException("Wrong header!");
        ErrorResponse thrownException = handler.handleItemHeaderException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }

    @Test
    void handleNotSupportedStateException() {
        UnsupportedStatusException exception = new UnsupportedStatusException("Unsupported status!");
        ErrorResponse thrownException = handler.handleNotSupportedStateException(exception);

        assertEquals(exception.getMessage(), thrownException.getError());
    }
}