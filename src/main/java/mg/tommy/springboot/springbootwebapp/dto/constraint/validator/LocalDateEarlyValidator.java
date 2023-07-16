package mg.tommy.springboot.springbootwebapp.dto.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class LocalDateEarlyValidator implements ConstraintValidator<Early, LocalDate> {
    private Period period;
    @Override
    public void initialize(Early constraintAnnotation) {
        period = Period.of(constraintAnnotation.years(), constraintAnnotation.months(), constraintAnnotation.days());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null)
            return true;
        LocalDate maxDate = LocalDate.now().minus(period);
        return localDate.isBefore(maxDate) || localDate.isEqual(maxDate);
    }
}
