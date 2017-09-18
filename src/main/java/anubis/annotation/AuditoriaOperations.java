package anubis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AuditoriaOperations {

	String insert() default "ADD";
	String update() default "EDIT";
	String delete() default "DELETE";
	
}
