package anubis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EnumerationValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumerationValid {

	String message() default "Tipo não encontrado";
	Class<? extends Enum<?>> enumeration();
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
}

