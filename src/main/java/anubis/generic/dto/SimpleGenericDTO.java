package anubis.generic.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.interfaces.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SimpleGenericDTO<T extends SimpleGenericBean> implements DTO, Cloneable {

	private static final long serialVersionUID = 960815050672107452L;
	
	protected Long id;

	public SimpleGenericDTO() {
		super();
	}
	
	public SimpleGenericDTO(T bean) {
		super();
		this.id = bean.getId();
	}

	public T converter() {
		T bean = null;
		try {
			bean = getClazz().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		return converter(bean);
	}
	
	public T converter(T bean) {
		if(bean.getId() == null) {
			bean.setId(id);
		}
		return bean;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public SimpleGenericDTO generate(SimpleGenericBean bean) throws Exception {
		try {
			return getClass().getConstructor(bean.getClass()).newInstance(bean);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw e;
		}
		//return null;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getClazz() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SimpleGenericDTO<T> clone() {
		SimpleGenericDTO<T> clone = null;
		try {
			clone = (SimpleGenericDTO<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			//FIXME PHILIPE ODIN LOGGER
			//			OdinLogger.error("Não foi possível clonar o objeto", e);
		}
		return clone;
	}
	
}
