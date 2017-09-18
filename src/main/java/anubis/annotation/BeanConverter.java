package anubis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import anubis.generic.bean.SimpleGenericBean;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanConverter {

	Class<? extends SimpleGenericBean> bean();
	String field() default "";
	
}
