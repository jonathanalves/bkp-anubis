package anubis.utils;

import anubis.generic.bean.SimpleGenericBean;

import java.util.function.Function;

public class  ObjectUtils {

    public static boolean equalsAttr(Function<SimpleGenericBean, Object> getAtributes, SimpleGenericBean bean1, SimpleGenericBean bean2) {
        Object attr1 = getAtributes.apply(bean1);
        Object attr2 = getAtributes.apply(bean2);
        return attr1.equals(attr2);
    }
}
