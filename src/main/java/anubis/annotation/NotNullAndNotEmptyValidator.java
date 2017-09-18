package anubis.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullAndNotEmptyValidator implements ConstraintValidator<NotNullAndNotEmpty, Object> {

    @Override
    public void initialize(NotNullAndNotEmpty constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null || value.toString().trim().isEmpty()) {
            return false;
        }
        return true;
    }

}
