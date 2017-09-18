package anubis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ObrigatorioValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ObrigatorioValid {

	String message() default "O campo \"{name}\" é obrigatório";
    
    Class<?>[] groups() default {};
      
    Class<? extends Payload>[] payload() default {};

	String name() default "";
	
}
