package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = FilmDateValidator.class)
public @interface IsAfter {
    String message() default "{message.key}";
    String current();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
