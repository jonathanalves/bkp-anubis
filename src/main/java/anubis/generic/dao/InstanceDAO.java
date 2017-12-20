package anubis.generic.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("instanceDAO")
public abstract class InstanceDAO<T> {

	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void executeQuery(String query) {
		(getEntityManager().createNativeQuery(query)).executeUpdate();
	}
	
	@SuppressWarnings("rawtypes")
	public List returnMappedQuery(Query query){
		org.hibernate.Query nativeQuery = ((org.hibernate.jpa.HibernateQuery) query).getHibernateQuery();
		nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return nativeQuery.list();
	}

	@SuppressWarnings("rawtypes")
	public List returnAliasToBeanQuery(Query query, Class<?> clazz){
		org.hibernate.Query nativeQuery = ((org.hibernate.jpa.HibernateQuery) query).getHibernateQuery();
		nativeQuery.setResultTransformer(Transformers.aliasToBean(clazz));
		return nativeQuery.list();
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> returnIndexedMap(Query query){
		org.hibernate.Query nativeQuery = ((org.hibernate.jpa.HibernateQuery) query).getHibernateQuery();
		nativeQuery.setResultTransformer(Transformers.TO_LIST);
		
		List<List<Object>> result = nativeQuery.list();
		
		HashMap<String,String> map = new HashMap<String,String>();
		for(List<Object> x: result){ 
		     map.put(x.get(0).toString(),x.get(1).toString());
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getClazz() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T findById(Object id) {
		return getEntityManager().find(getClazz(), id);
	}

	@SuppressWarnings("hiding")
	public <T> T findById(Object id, Class<T> clazz) {
		return getEntityManager().find(clazz, id);
	}
	
	
	public T persist(T t) throws SQLException {
		try {
			getEntityManager().persist(t);
			return t;
		} catch (Exception e) {
			System.out.println("SQL Persist " + t.toString());
			throw new SQLException(e.getMessage(), e);
		}
	}

	public void remove(Object obj) throws DataIntegrityViolationException {
		try {
			obj = getEntityManager().merge(obj);
			getEntityManager().remove(obj);
			getEntityManager().flush();
		} catch (DataIntegrityViolationException e) {
			System.out.println("SQL Remove " + obj.toString());
			throw new DataIntegrityViolationException(e.getMessage(), e.getRootCause());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class, Throwable.class}, timeout = 120, propagation = Propagation.REQUIRED)
	public T merge(T t) throws SQLException {
		try {
			return getEntityManager().merge(t);
		} catch (Exception e) {
			System.out.println("SQL Merge " + t.toString());
			throw new SQLException(e.getMessage(), e);
		}
	}

}
