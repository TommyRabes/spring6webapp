package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

}
