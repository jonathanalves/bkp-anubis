package anubis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import anubis.enumeration.system.EnumGender;
import anubis.generic.dto.SimpleGenericDTO;
import anubis.generic.interfaces.DTO;

@Retention(RetentionPolicy.RUNTIME)
public @interface BeanProperties {

	EnumGender gender() default EnumGender.MALE;
	
	String slug() default "bean_without_slug";
	
	String label();
	
	Class<? extends DTO> dtoClass() default SimpleGenericDTO.class;
	
}
