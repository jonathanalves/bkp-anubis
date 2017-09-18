package anubis.generic.dto;

import anubis.generic.bean.AuditoriaBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditoriaDTO extends SimpleGenericDTO<AuditoriaBean>{

	private static final long serialVersionUID = -5308356397563774982L;
	
	private Long usuarioId;
	
	private String usuarioNome;
	
	private String dataAcao;
	private String acao;

	private Object antigo;
	private Object novo;

}
