package mg.tommy.springboot.springbootwebapp.model.dto.constraint.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ZonedDateTimeEarlyValidator.class, LocalDateTimeEarlyValidator.class, LocalDateEarlyValidator.class })
public @interface Early {
    int years() default 0;
    int months() default 0;
    int days() default 0;
    int amountOfTime() default 0;
    ChronoUnit unitOfTime() default ChronoUnit.DAYS;
    String message() default "must be at least {years} years, {months} months, {days} days and {amountOfTime} {unitOfTime} earlier than the current date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
