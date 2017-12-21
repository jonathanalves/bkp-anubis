package anubis.generic.dao;

import anubis.annotation.BeanProperties;
import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dto.QueryDataTablesDTO;
import anubis.generic.dto.SimpleGenericDTO;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class GenericDAO <T extends SimpleGenericBean> extends InstanceDAO<T> {

	@SuppressWarnings("unchecked")
	public List<Long> getListIds(String listName, Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT x.id FROM ").append(getClazz().getName()).append(" e ");
		hql.append(" JOIN e.").append(listName).append(" as x ");
		hql.append(" WHERE e.id = :id ");
		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Long> listAllIds(Boolean isAtivo, String orderBy, Integer page, Integer countRecord) {
		StringBuilder hql = new StringBuilder("SELECT id FROM " + getClazz().getName());
		if(isAtivo != null) {
			hql.append(" WHERE isAtivo = :isAtivo ");
		}
		if(orderBy != null){
			hql.append(" ORDER BY " + orderBy + " ASC");
		}
		Query query = getEntityManager().createQuery(hql.toString());
		if(isAtivo != null){
			query.setParameter("isAtivo", isAtivo);
		}
		if(page != null && countRecord != null) {
			query.setFirstResult(countRecord * page);
			query.setMaxResults(countRecord);
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listAll(Boolean isAtivo, String orderBy) {
		StringBuilder hql = new StringBuilder("FROM " + getClazz().getName());
		if(isAtivo != null) {
			hql.append(" WHERE isAtivo = :isAtivo ");
		}
		if(orderBy != null){
			hql.append(" ORDER BY " + orderBy + " ASC");
		}
		Query query = getEntityManager().createQuery(hql.toString());
		if(isAtivo != null){
			query.setParameter("isAtivo", isAtivo);
		}
		return query.getResultList();
	}
	
	public List<T> listAll() {
		return listAll(null, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listById(List<Long> ids) {
		if(ids == null || ids.isEmpty()) {
			return null;
		}
		StringBuilder hql = new StringBuilder("FROM " + getClazz().getName());
		hql.append(" WHERE id IN (:ids) ");

		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("ids", ids);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public List search(String field, String queryField, Integer countRecord) {
		StringBuilder hql = new StringBuilder();
		hql.append( getHqlDtoBase() );
		hql.append(" WHERE LOWER(e.");
		hql.append(field);
		hql.append(") LIKE :query ");
		hql.append(" ORDER BY e.");
		hql.append(field);
		hql.append(" ASC ");
		
		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("query", '%'+queryField.toLowerCase()+'%');
		query.setMaxResults(countRecord);
		
		return query.getResultList();
	}
	
	/* ********************* DTO'S ********************* */
	
	public String getHqlDtoBase(){
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT new Map( ");
		hql.append( getMapDTO() );
		hql.append(" ) ");
		hql.append("FROM ");
		hql.append(getClazz().getName());
		hql.append(" e ");
		hql.append(getJoinsDTO());
		return hql.toString();
	}

	public String getMapDTO() {
		StringBuilder map = new StringBuilder();
		map.append(" e.id as id, ");
		map.append(" e.nome as nome, ");
		map.append(" e.isAtivo as isAtivo ");
		return map.toString();
	}

	public String getSimpleMapDTO() {
		StringBuilder map = new StringBuilder();
		map.append(" e.id as id, ");
		map.append(" e.nome as nome, ");
		map.append(" e.isAtivo as isAtivo ");
		return map.toString();
	}

	public String getJoinsDTO() {
		return "";
	}

	public String getSimpleJoinsDTO() {
		return "";
	}
	
	@SuppressWarnings({ "unchecked" })
	public SimpleGenericDTO<T> findDTO(Object id) {
		try{
			StringBuilder hql = new StringBuilder();
			hql.append( getHqlDtoBase() );
			hql.append("WHERE e.id = :id ");
			Query query = getEntityManager().createQuery(hql.toString());
			query.setParameter("id", id);		
			Map<String, Object> obj = (Map<String, Object>) query.getSingleResult();
			return converterDTO(obj);
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<SimpleGenericDTO<T>> listDTO(Boolean isAtivo, String order) {
		try{
			StringBuilder hql = new StringBuilder();
			hql.append(getHqlDtoBase());
			if(isAtivo != null) {
				hql.append(" WHERE e.isAtivo = :isAtivo ");
			}
			if(order != null) {
				hql.append(" ORDER BY ").append("e." + order).append(" ASC");
			}
			Query query = getEntityManager().createQuery(hql.toString());
			if(isAtivo != null) {
				query.setParameter("isAtivo", isAtivo);
			}
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<SimpleGenericDTO<T>> listSimpleDTO(Boolean isAtivo, String order) {
		try{
			StringBuilder hql = new StringBuilder();

			hql.append(" SELECT new Map( ");
			hql.append( getSimpleMapDTO() );
			hql.append(" ) ");
			hql.append("FROM ");
			hql.append(getClazz().getName());
			hql.append(" e ");
			hql.append(getSimpleJoinsDTO());
			
			if(isAtivo != null) {
				hql.append(" WHERE e.isAtivo = :isAtivo ");
			}
			if(order != null) {
				hql.append(" ORDER BY ").append("e." + order).append(" ASC");
			}
			Query query = getEntityManager().createQuery(hql.toString());
			if(isAtivo != null) {
				query.setParameter("isAtivo", isAtivo);
			}
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public SimpleGenericDTO<T> converterDTO(Object obj){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map =  mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
		return (SimpleGenericDTO<T>) mapper.convertValue(map, getClazz().getAnnotation(BeanProperties.class).dtoClass());
	}
	
	/* ********************* PAGINATION DTO'S ********************* */
	
	public Integer getDTOsQuantity(QueryDataTablesDTO queryDT){
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT CAST( COALESCE ( count(*), 0) AS integer) ");
		hql.append(getDTOsHQL(queryDT));
		Query query = getDTOsQuery( queryDT, hql.toString() );
		return (Integer) query.getSingleResult();
	}

	public String getOrderPagination(QueryDataTablesDTO queryDT){
		if(queryDT.getOrderBy() != null){
			return " ORDER BY " + queryDT.getOrderBy() + " " + queryDT.getOrderByDirection() + " NULLS LAST";
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public List getDTOs(QueryDataTablesDTO queryDT){
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT new Map( ");
		hql.append( getMapDTO() );
		hql.append(" ) ");
		hql.append(getDTOsHQL(queryDT));

		//=============================== ORDENAÇÃO ======================================
		hql.append(getOrderPagination(queryDT) );

		//============================= CRIA QUERY ========================================
		Query query = getDTOsQuery( queryDT, hql.toString() );
		
		//=============================== SETA PAGINAÇÃO ======================================
		if(queryDT.getQuantidade() > 0) {
			query.setFirstResult((queryDT.getPagina() - 1) * queryDT.getQuantidade() );
			query.setMaxResults(queryDT.getQuantidade());
		}
		
		return query.getResultList();
	}
	
	private String getDTOsHQL(QueryDataTablesDTO queryDT) {
		StringBuilder hql = new StringBuilder();
		hql.append("FROM ");
		hql.append(getClazz().getName());
		hql.append(" e ");
		hql.append(getJoinsDTO());

		// DATA
		hql.append(" WHERE 1=1 ");
		
		hql.append(queryDT.getWhere());

		// busca escrita
		if( !queryDT.getSearch().trim().isEmpty() ) {
			hql.append( getWherePagination(queryDT.getSearch().trim()) );
		}

		hql.append( getDTOsHqlOthers(queryDT) );
		
		return hql.toString();
		
	}

	public String getDTOsHqlOthers(QueryDataTablesDTO queryDT){
		return "";
	}

	// método onde o search da paginação irá realizar a busca dos registros
	public String getWherePagination(String search){
		return " AND lower(e.nome) LIKE :search " ;
	}
	
	public Query getDTOsQuery(QueryDataTablesDTO queryDT, String hql) {
		Query query = getEntityManager().createQuery(hql);
		if( !queryDT.getSearch().trim().isEmpty() ){
			query.setParameter("search", "%" + queryDT.getSearch().toLowerCase().trim() + "%");
		}
		return query;
	}
	
	@Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
	public boolean existByField(String field, Object fieldValue, Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT COUNT(*) FROM ");
		hql.append(getClazz().getName());
		hql.append(" WHERE UPPER( ");
		hql.append(field);
		hql.append(" ) = UPPER(:value) ");
		if(id != null) {
			hql.append(" AND id <> :id ");
		}
		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("value", fieldValue);
		if(id != null) {
			query.setParameter("id", id);
		}
		Long count = (Long) query.getSingleResult();
		return count > 0;
	}
	
	public Long maxId() {
		Long maxId = 0l;
		try {
			String hql = "SELECT COALESCE(MAX(id), 0) FROM " + getClazz().getName();
			Query query = getEntityManager().createQuery(hql);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (NoResultException e) { }
		return maxId;
	}
	
}
