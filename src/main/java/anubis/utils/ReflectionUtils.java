package anubis.utils;


import anubis.annotation.BeanProperties;
import anubis.generic.bean.SimpleGenericBean;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static String getLabelBean(Class<? extends SimpleGenericBean> classBean) {
        return classBean.getAnnotation(BeanProperties.class).label();
    }

    public static Field getFieldFromType(Class<?> clazz, Class<?> fieldFromType) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == fieldFromType) {
                return field;
            }
        }
        throw new RuntimeException("Não existe atributo do tipo " + fieldFromType.getName() + " na classe " + clazz.getName());
    }
}
