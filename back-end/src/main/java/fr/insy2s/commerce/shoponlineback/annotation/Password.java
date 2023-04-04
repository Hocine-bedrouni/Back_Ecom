package fr.insy2s.commerce.shoponlineback.annotation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordConstraintsValidator.class)
public @interface Password {

    String message() default "Password should be valid ";

    Class[] groups() default {};

    Class[] payload() default {};
}
