package anubis.generic.bean;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import anubis.annotation.BeanProperties;
import anubis.enumeration.system.EnumGender;
import anubis.generic.dto.AuditoriaDTO;
import anubis.hibernate.StringJsonUserType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Actus 
 * Description: Registra as ações de configuração do sistema.
 */
@Getter
@Setter
@Entity(name = "Auditoria")
@BeanProperties(slug = "auditoria", label = "bean.auditoria", gender = EnumGender.FEMALE, dtoClass = AuditoriaDTO.class)
@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
public class AuditoriaBean extends SimpleGenericBean {

	private static final long serialVersionUID = 6342644424394334479L;
	
	@Column(name = "usuario_id", nullable = false)
	private Long usuarioId;
	
	@Column(name = "operacao", nullable = false)
	private String operacao;

	// Data no momento do cadastro do documento
	@Column(name = "data_acao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataAcao;

	@Column(name = "antigo", columnDefinition = "json")
	@Type(type = "StringJsonObject")
	private String antigo;
	
	@Column(name = "novo", columnDefinition = "json")
	@Type(type = "StringJsonObject")
	private String novo;
	
	@Column(name = "objeto_id")
	private Long objetoId;

	@Column(name = "objeto_tipo")
	private String objetoTipo;
	
	public AuditoriaBean() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((antigo == null) ? 0 : antigo.hashCode());
		result = prime * result + ((dataAcao == null) ? 0 : dataAcao.hashCode());
		result = prime * result + ((novo == null) ? 0 : novo.hashCode());
		result = prime * result + ((objetoId == null) ? 0 : objetoId.hashCode());
		result = prime * result + ((objetoTipo == null) ? 0 : objetoTipo.hashCode());
		result = prime * result + ((operacao == null) ? 0 : operacao.hashCode());
		result = prime * result + ((usuarioId == null) ? 0 : usuarioId.hashCode());
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
		AuditoriaBean other = (AuditoriaBean) obj;
		if (antigo == null) {
			if (other.antigo != null)
				return false;
		} else if (!antigo.equals(other.antigo))
			return false;
		if (dataAcao == null) {
			if (other.dataAcao != null)
				return false;
		} else if (!dataAcao.equals(other.dataAcao))
			return false;
		if (novo == null) {
			if (other.novo != null)
				return false;
		} else if (!novo.equals(other.novo))
			return false;
		if (objetoId == null) {
			if (other.objetoId != null)
				return false;
		} else if (!objetoId.equals(other.objetoId))
			return false;
		if (objetoTipo == null) {
			if (other.objetoTipo != null)
				return false;
		} else if (!objetoTipo.equals(other.objetoTipo))
			return false;
		if (operacao == null) {
			if (other.operacao != null)
				return false;
		} else if (!operacao.equals(other.operacao))
			return false;
		if (usuarioId == null) {
			if (other.usuarioId != null)
				return false;
		} else if (!usuarioId.equals(other.usuarioId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuditoriaBean [usuarioId=" + usuarioId + ", operacao=" + operacao + ", dataAcao=" + dataAcao
				+ ", antigo=" + antigo + ", novo=" + novo + ", objetoId=" + objetoId + ", objetoTipo=" + objetoTipo
				+ "]";
	}
	
}
