package com.example.test.exception;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleSQLException(
        SQLException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL exception error occurred"));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Data integrity violation error occurred"));
    }

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    // InvalidDataAccessApiUsageException

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInvalidDataAccessApiUsageException(
            InvalidDataAccessApiUsageException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid data access error occurred"));
    }

    //

   

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleJsonParseExceptionException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "One of the fields is missing its value"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundExceptionException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Entity not found"));
    }



    //
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Number format error " + exception.getMessage().toLowerCase() + ", integer value expected."));
    }

 
    // 400
    @ExceptionHandler({ BadRequestException.class })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequests(final RuntimeException ex) {
        return throwException(HttpStatus.BAD_REQUEST, ex);
    }

    // 404
    @ExceptionHandler({ NotFoundException.class })
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFound(final RuntimeException ex) {
        return throwException(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleUsernameNotFoundException(final RuntimeException ex) {
        return throwException(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ExceptionHandler({ InvalidInputException.class })
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleInvalidInput(final RuntimeException ex) {
        return throwException(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    // 401
    @ExceptionHandler({ UnauthorizedException.class })
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorized(final RuntimeException ex, final WebRequest request) {
        return throwException(HttpStatus.UNAUTHORIZED, ex);
    }

    //

    private ResponseEntity<Object> throwException(HttpStatus statusCode, Exception ex) {
        return ResponseEntity.status(statusCode).body(new ErrorResponse(statusCode.value(), ex.getMessage()));
    }

}
