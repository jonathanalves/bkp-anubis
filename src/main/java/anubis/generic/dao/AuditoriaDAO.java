package anubis.generic.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import anubis.generic.bean.AuditoriaBean;

@Repository("auditoriaDAO")
public class AuditoriaDAO extends GenericDAO<AuditoriaBean> {
	
	@SuppressWarnings("unchecked")
	public List<AuditoriaBean> getAuditorias(String tipo, Long id) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT a FROM Auditoria a ");
		hql.append(" WHERE LOWER(a.objetoTipo) = :tipo ");
		if(id != null){
			hql.append(" AND a.objetoId = :objetoId ");
		}
		
		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("tipo", tipo.toLowerCase());
		if(id != null){
			query.setParameter("objetoId", id);
		}
		
		return query.getResultList();
	}
	
}
