package mg.tommy.springboot.springbootwebapp.controller.api;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(fieldError.getField(), requireNonNull(fieldError.getDefaultMessage())))
                .collect(toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleTypeConversionError(HttpMessageNotReadableException exception) {
        log.warn(exception.getMessage());
        return ResponseEntity.badRequest().body(Map.of("type", "TypeMismatching", "message", "Type conversion failed"));
    }

    /**
     * It's technically impossible to get into this handler since our entity has the exact same set of constraints
     * as its dto counterpart. Indeed, bean validation checks will occur at the controller level
     * then it's almost sure that all data forwarded to our repository are sanitized
     * Nonetheless there are case where validation is tied to business logic
     * so, we might report data inconsistency at the service layer
     * in this case, if we are in a middle of an ongoing transaction,
     * we will probably end up in this method
     * @param exception
     * @return
     */
    @ExceptionHandler
    public ResponseEntity handleTransactionError(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder badRequestBuilder = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException constraintViolationException) {
            Set<Map<String, String>> errors = constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(constraintViolation -> Map.of(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
                    .collect(Collectors.toSet());
            return badRequestBuilder.body(errors);
        }

        return badRequestBuilder.build();
    }

}
