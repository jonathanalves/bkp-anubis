package anubis.generic.test;

import java.lang.reflect.ParameterizedType;

import org.springframework.stereotype.Component;

import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dto.SimpleGenericDTO;
import anubis.utils.DateUtils;

@Component("generatorGeneric")
public abstract class GeneratorGeneric<Gbean extends SimpleGenericBean, Gdto extends SimpleGenericDTO<Gbean>> extends GeneratorValues {
	
	@SuppressWarnings("unchecked")
	protected Class<Gdto> getClazz() {
		return (Class<Gdto>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected String generatorName(String metodo) {
		String random = DateUtils.getCalendar().getTimeInMillis() + "" + (Math.random() * 60);
		StringBuilder name = new StringBuilder();
		name.append(metodo);
		name.append(" ");
		name.append(getClazz().getSimpleName().replace("DTO", "").replace("Bean", ""));
		name.append(" ");
		name.append(random);
		return name.toString();
	}
	
	public abstract Gdto newRegistro(String metodo) throws Exception;
	
	public Gdto editRegistro(Gbean bean, Gdto dto) {
		dto.setId(bean.getId());
		return dto;
	}
	
	public <T extends Enum<?>> Enum<?> generateEnum(Class<? extends Enum<?>> enumClass) {
        Enum<?> resultado = null;
        try {
            Enum<?>[] enumValArr = enumClass.getEnumConstants();
            int valor = generatorIntegerValue(0, enumValArr.length-1);
            resultado = enumValArr[valor];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
	
}
