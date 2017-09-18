package anubis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListBeanConverter {

	/** Nome do campo do bean que representa a lista */
	String field() default "";
	
	/** Nome do campo no item da lista que representa o DTO raíz (pai) */
	String referecedBy() default "";
	
}
