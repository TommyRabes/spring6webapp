package mg.tommy.springboot.springbootwebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Introduced in Spring 3.0
// As of Spring 5.0, the preferred method is to throw a ResponseStatusException instead of creating
// an annotated exception like this, since the error code and message are tightly coupled to the class
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Author not found")
public class AuthorNotFoundException extends RuntimeException {
}
