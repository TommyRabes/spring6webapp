package mg.tommy.springboot.springbootwebapp.dto.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class LocalDateTimeEarlyValidator implements ConstraintValidator<Early, LocalDateTime> {
    private Period period;
    private Duration duration;

    @Override
    public void initialize(Early constraintAnnotation) {
        period = Period.of(constraintAnnotation.years(), constraintAnnotation.months(), constraintAnnotation.days());
        duration = Duration.of(constraintAnnotation.amountOfTime(), constraintAnnotation.unitOfTime());
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null)
            return true;
        LocalDateTime maxDatetime = LocalDateTime.now().minus(period).minus(duration);
        return localDateTime.isBefore(maxDatetime) || localDateTime.isEqual(maxDatetime);
    }
}
