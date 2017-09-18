package anubis.generic.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import anubis.annotation.NotNullAndNotEmpty;
import anubis.generic.bean.ConfigurationGenericBean;
import anubis.generic.bean.SimpleGenericBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class ConfigurationGenericDTO<T extends ConfigurationGenericBean> extends SimpleGenericDTO<T> {

	private static final long serialVersionUID = 609897038377567657L;

	@NotNullAndNotEmpty(message = "crud.validation.required#{label.nome}")
	@Size(max = 250, message = "crud.validation.string.size.max#{label.nome}#250")
	protected String nome;
	
	@NotNull(message = "crud.validation.required#{label.is.ativo}")
	protected Boolean isAtivo;
	
	public ConfigurationGenericDTO() {
		super();
	}
	
	public ConfigurationGenericDTO(T bean) {
		super(bean);
		this.nome = bean.getNome();
		this.isAtivo = bean.getIsAtivo()!= null ? bean.getIsAtivo() : null;
	}
	
	public T converter(T bean) {
		super.converter(bean);
		bean.setNome(nome);
		bean.setIsAtivo(isAtivo);
		return bean;
	}

	@SuppressWarnings("unchecked")
	public Class<T> getClazz() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings({ "rawtypes" })
	public ConfigurationGenericDTO generate(SimpleGenericBean bean) {
		try {
			return getClass().getConstructor(bean.getClass()).newInstance(bean);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
