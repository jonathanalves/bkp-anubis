package anubis.generic.bean;

import anubis.generic.interfaces.Bean;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class SimpleGenericBean implements Bean, Cloneable {

	private static final long serialVersionUID = -7228317077792955343L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	protected Long id;

	public SimpleGenericBean() {
		super();
	}
	
	public SimpleGenericBean(Long id) {
		super();
		this.id = id;
	}
	
	@Override
	public SimpleGenericBean clone() {
		SimpleGenericBean bean= null;
		try {
			bean = (SimpleGenericBean) super.clone();
		} catch (CloneNotSupportedException e) {
			//FIXME PHILIPE COLOCAR O LOGER DEPOIS
//			OdinLogger.error("Não foi possível clonar o objeto", e);
		}
		return bean;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleGenericBean other = (SimpleGenericBean) obj;
		if (id == null) {
            return other.id == null;
		} else return id.equals(other.id);
    }

	@Override
	public String toString() {
		return "SimpleGenericBean [id=" + id + "]";
	}
	
	

}
