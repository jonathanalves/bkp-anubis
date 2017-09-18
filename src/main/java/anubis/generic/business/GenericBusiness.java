package anubis.generic.business;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import anubis.MessageSystem;
import anubis.annotation.BeanProperties;
import anubis.generic.bean.ConfigurationGenericBean;
import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dao.GenericDAO;
import anubis.generic.dto.QueryDataTablesDTO;
import anubis.generic.dto.SimpleGenericDTO;
import anubis.response.ResponseException;

public abstract class GenericBusiness <Gbean extends SimpleGenericBean, DAO extends GenericDAO<Gbean>> {

	@Autowired
	protected DAO dao;

	public DAO getDAO() {
		return this.dao;
	}
	
	
	//##################################### PROPERTIES #####################################
	
	@SuppressWarnings("unchecked")
	protected Class<Gbean> getClazz() {
		return (Class<Gbean>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected Long getAttributeId(Gbean t) {
		return t.getId();
	}

	protected String getValueAttributeUniqueField(Gbean t) {
		if(t instanceof ConfigurationGenericBean){
			return ((ConfigurationGenericBean) t).getNome();
		}
		return null;
	}
	
	protected String getAttributeUniqueField() {
		return "nome";
	}

	public String getLabelBean(Gbean bean) {
		BeanProperties properties = getClazz().getAnnotation(BeanProperties.class);
		return properties.label();
	}

	public String getSlugBean() {
		return getSlugBean(null);
	}

	public String getSlugBean(Gbean bean) {
		BeanProperties properties = getClazz().getAnnotation(BeanProperties.class);
		return properties.slug();
	}

	public boolean isMale(Gbean bean) {
		BeanProperties properties = getClazz().getAnnotation(BeanProperties.class);
		return properties.gender().isMale();
	}
	
	public String messageNameAttributeUnique(Gbean t) {
		return "label."+getAttributeUniqueField();
	}
	
	protected void generateUniqueException(Gbean t) {
		String message = isMale(t) ? "crud.generic.exist.unique.masculino" : "crud.generic.exist.unique.feminino";
		throw new ResponseException(message, messageNameAttributeUnique(t), getValueAttributeUniqueField(t), getLabelBean(t));
	}
	
	//##################################### INSERT #####################################
	
	public void beforeInsert(Gbean t) throws Exception {
		if(getValueAttributeUniqueField(t) != null){
			boolean exist = dao.existByField(getAttributeUniqueField(), getValueAttributeUniqueField(t), t.getId());
			if(exist){
				generateUniqueException(t);
			}
		}
	}
	
	public Gbean insert(Gbean t) throws Exception {
		beforeInsert(t);
		dao.persist(t);
		afterInsertSuccess(t);
		return t;
	}
	
	public void afterInsertSuccess(Gbean t) throws Exception {  }
	
	
	//##################################### EDIT #####################################
	
	public void beforeEdit(Gbean t, Gbean tOld) throws Exception {
		if(getValueAttributeUniqueField(t) != null){
			boolean exist = dao.existByField(getAttributeUniqueField(), getValueAttributeUniqueField(t), t.getId());
			if(exist){
				generateUniqueException(t);
			}
		}
	}
	
	public Gbean edit(Gbean t, Gbean tOld) throws Exception {
		beforeEdit(t, tOld);
		dao.merge(t);
		afterEditSuccess(t, tOld);
		return t;
	}
	
	public void afterEditSuccess(Gbean t, Gbean tOld) throws Exception {  }
	
	
	//##################################### REMOVE #####################################
	
	public boolean validRemove(Gbean t) {
		return true;
	}
	
	public void beforeRemove(Gbean t) throws Exception {  }
	
	public void remove(Gbean t) throws Exception {
		try {
			beforeRemove(t);
			validRemove(t);
			dao.remove(t);
			afterRemoveSuccess(t);
		} catch (DataIntegrityViolationException e) {
			String message = isMale(t) ? "crud.generic.erro.excluir.masculino" : "crud.generic.erro.excluir.feminino";
			throw new ResponseException(e, message, MessageSystem.getMessage(getLabelBean(t)).toLowerCase());
		}
	}
	
	public void afterRemoveSuccess(Gbean t) throws Exception {  }
	
	
	//##################################### GETS AND LISTS #####################################
	
	public Gbean getById(Long id) throws ResponseException {
		Gbean t = id != null ? dao.findById(id) : null;
		if (t == null) {
			String mensagem = isMale(t)? "crud.generic.nao.encontrado.masculino" : "crud.generic.nao.encontrado.feminino";
			throw new ResponseException(mensagem, getLabelBean(null));
		}
		return t;
	}

	public SimpleGenericDTO<?> getDTO(Long id) throws ResponseException {
		SimpleGenericDTO<?> t = id != null ? dao.findDTO(id) : null;
		if (t == null) {
			String mensagem = isMale(null)? "crud.generic.nao.encontrado.masculino" : "crud.generic.nao.encontrado.feminino";
			throw new ResponseException(mensagem,getLabelBean(null));
		}
		return t;
	}
	
	public List<?> search(String field, String queryField, Integer countRecord) {
		return dao.search(field, queryField, countRecord);
	}
	
	public Map<String, Object> pagination(QueryDataTablesDTO queryDT) {
		Map<String, Object> dto = new HashMap<>();

		dto.put("draw", queryDT.getDraw());
		
		Integer quantidadeRegistros = dao.getDTOsQuantity(queryDT);
		dto.put("recordsTotal", quantidadeRegistros);
		dto.put("recordsFiltered", quantidadeRegistros);
		
		dto.put("data", dao.getDTOs(queryDT));
		
		return dto;
	}
	
	public List<Gbean> listById(List<Long> ids) {
		return dao.listById(ids);
	}

	public List<?> listAllDTO(Boolean isAtivo, String order) {
		return dao.listDTO(isAtivo, order);
	}
	
	public List<?> listAllDTO(Boolean isAtivo) {
		return dao.listDTO(isAtivo, null);
	}

	public List<?> listSimpleDTO(Boolean isAtivo, String order) {
		return dao.listSimpleDTO(isAtivo, order);
	}

	//##################################### UPDATE PARENT LIST #####################################
	public void updateParentList(Collection<Gbean> oldList, Collection<Gbean> newList) throws Exception {

        if(newList != null) {
            for(Gbean bean : newList) {
                if(bean.getId() == null) {
                	insert(bean);
                } else {
                	edit(bean, null);
                    if(oldList.contains(bean)) {
                    	oldList.remove(bean);
                    }
                }
            }
        }
        
        for(Gbean valorAdicionalOld : oldList) {
        	Gbean tmp = dao.findById(valorAdicionalOld.getId());
        	remove(tmp);
        }
		
	}
	
}
