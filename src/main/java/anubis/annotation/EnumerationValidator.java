package anubis.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumerationValidator implements ConstraintValidator<EnumerationValid, String> {
	
	List<String> valueList = null;
	
	@Override
	public void initialize(EnumerationValid constraintAnnotation) {
		valueList = new ArrayList<>();
	    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumeration();

	    @SuppressWarnings("rawtypes")
	    Enum[] enumValArr = enumClass.getEnumConstants();

	    for(@SuppressWarnings("rawtypes")
	    Enum enumVal : enumValArr) {
	      valueList.add(enumVal.toString().toUpperCase());
	    }
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.isEmpty()) {
			return true;
		}
        return valueList.contains(value.toUpperCase());
    }

}