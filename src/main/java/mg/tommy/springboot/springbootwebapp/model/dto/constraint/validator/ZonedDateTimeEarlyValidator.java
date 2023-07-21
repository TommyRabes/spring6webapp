package mg.tommy.springboot.springbootwebapp.model.dto.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

public class ZonedDateTimeEarlyValidator implements ConstraintValidator<Early, ZonedDateTime> {
    private Period period;
    private Duration duration;

    @Override
    public void initialize(Early constraintAnnotation) {
        period = Period.of(constraintAnnotation.years(), constraintAnnotation.months(), constraintAnnotation.days());
        duration = Duration.of(constraintAnnotation.amountOfTime(), constraintAnnotation.unitOfTime());
    }

    @Override
    public boolean isValid(ZonedDateTime zonedDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (zonedDateTime == null)
            return true;
        ZonedDateTime maxDatetime = ZonedDateTime.now(zonedDateTime.getZone()).minus(period).minus(duration);
        return zonedDateTime.isBefore(maxDatetime) || zonedDateTime.isEqual(maxDatetime);
    }
}
