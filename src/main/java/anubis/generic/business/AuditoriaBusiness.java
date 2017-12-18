package anubis.generic.business;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import anubis.MessageSystem;
import anubis.generic.bean.AuditoriaBean;
import anubis.generic.dao.AuditoriaDAO;
import anubis.generic.dto.AuditoriaDTO;
import anubis.response.ResponseException;
import anubis.utils.DateUtils;

@Service("auditoriaBusiness")
public class AuditoriaBusiness extends GenericBusiness<AuditoriaBean, AuditoriaDAO> {
	
	public void inserirAuditoria(String operacao, Object antigo, Object novo, Long objetoId, Long usuarioId, String tipo) throws ResponseException, SQLException {
		AuditoriaBean auditoria = new AuditoriaBean();
		auditoria.setObjetoId(objetoId);
		auditoria.setUsuarioId(usuarioId);
		auditoria.setOperacao(operacao);
		auditoria.setDataAcao(DateUtils.getCalendar());
		auditoria.setObjetoTipo(tipo);
		
        auditoria.setAntigo(antigo);
        auditoria.setNovo(novo);

		dao.persist(auditoria);
	}
	
	public List<AuditoriaDTO> getAuditoria(String identificador, Long id) {
		if(id == null) {
			throw new ResponseException("business.auditoria.informe.id");
		}
		if(identificador == null || identificador.isEmpty()) {
			throw new ResponseException("business.auditoria.informe.identificador");
		}

		List<AuditoriaBean> auditorias = dao.getAuditorias(identificador, id);
		
		List<AuditoriaDTO> retorno = new ArrayList<>();
		
		for(AuditoriaBean auditoria : auditorias){
			
			AuditoriaDTO dto = new AuditoriaDTO();
			
			dto.setAcao(MessageSystem.getMessage("auditoria." + auditoria.getObjetoTipo() + "." + auditoria.getOperacao()));
			dto.setDataAcao(DateUtils.getDiaMesAnoHoraMinutoSegundo(auditoria.getDataAcao()));
			
			dto.setUsuarioId(auditoria.getUsuarioId());
			
			if(auditoria.getAntigo() != null){
				dto.setAntigo(auditoria.getAntigo());
			}

			if(auditoria.getNovo() != null){
				dto.setNovo(auditoria.getNovo());
			}
			
			retorno.add(dto);
		}
		
		return retorno;
	}

}
