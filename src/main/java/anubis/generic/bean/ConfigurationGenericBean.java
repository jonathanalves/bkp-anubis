package anubis.generic.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
@ToString(callSuper=true)
public abstract class ConfigurationGenericBean extends SimpleGenericBean {

	private static final long serialVersionUID = 1053774552537930442L;

	@Column(name = "nome", length = 250, nullable = false)
	protected String nome;
	
	@Column(name = "is_ativo", nullable = false, columnDefinition = "boolean NOT NULL DEFAULT true")
	protected Boolean isAtivo;

	public ConfigurationGenericBean() {
		super();
	}

	public ConfigurationGenericBean(Long id, String nome, Boolean isAtivo) {
		super(id);
		this.nome = nome;
		this.isAtivo = isAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isAtivo == null) ? 0 : isAtivo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigurationGenericBean other = (ConfigurationGenericBean) obj;
		if (isAtivo == null) {
			if (other.isAtivo != null)
				return false;
		} else if (!isAtivo.equals(other.isAtivo))
			return false;
		if (nome == null) {
            return other.nome == null;
		} else return nome.equals(other.nome);
    }
	
}
