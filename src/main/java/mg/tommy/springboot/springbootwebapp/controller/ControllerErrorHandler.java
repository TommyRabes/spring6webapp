package mg.tommy.springboot.springbootwebapp.controller;

import mg.tommy.springboot.springbootwebapp.exception.PlanNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// basePackageClasses is just a type-safe alternative to basePackages
// it actually handles all controllers within the same package as the provided class
@ControllerAdvice(basePackageClasses = PageErrorController.class)
public class ControllerErrorHandler {

    // Disable this handler as it takes precedence when we throw a ResponseStatusException
    // from one of the controllers, we want the default behaviour for a 404 error
    // @ExceptionHandler(RuntimeException.class)
    public String runtimeHandler(RuntimeException exception, Model model) {
        model.addAttribute("exceptionType", "RuntimeException");
        model.addAttribute("exceptionMessage", exception.getMessage());
        return "errorStackTrace";
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ErrorResponse handlePlanNotFound(PlanNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setProperty("message", exception.getMessage());
        return new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, exception);
    }
}
