package mg.tommy.springboot.springbootwebapp.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageErrorController {

    @RequestMapping("/runtimeException")
    public String errorState() {
        throw new RuntimeException("An unchecked exception occurred at runtime");
    }

    @RequestMapping("/illegalArgument")
    public String errorArgument() {
        throw new IllegalArgumentException("Exception thrown anyways");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private String illegalArgumentExceptionHandler(IllegalArgumentException exception, Model model) {
        model.addAttribute("exceptionType", "Illegal argument");
        model.addAttribute("exceptionMessage", exception.getMessage());
        return "errorStackTrace";
    }
}
