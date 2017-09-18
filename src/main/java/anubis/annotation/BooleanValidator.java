package anubis.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BooleanValidator implements ConstraintValidator<BooleanValid, Integer> {

	@Override
	public void initialize(BooleanValid annotation) {
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext ctx) {
		if(value == null){
            return false;
        }
		if (value==0) return true;
        if (value==1) return true;
        return false;
	}

}
