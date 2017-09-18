package anubis.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ObrigatorioValidator implements ConstraintValidator<ObrigatorioValid, Object> {

	@Override
	public void initialize(ObrigatorioValid annotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext ctx) {
		if(value == null || value.toString().trim().isEmpty()){
            return false;
        }
        return true;
	}

}
