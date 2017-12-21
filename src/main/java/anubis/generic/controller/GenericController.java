package anubis.generic.controller;

import anubis.annotation.AuditoriaOperations;
import anubis.annotation.BeanConverter;
import anubis.annotation.BeanProperties;
import anubis.annotation.IgnoreAuditoria;
import anubis.annotation.ListBeanConverter;
import anubis.enumeration.system.EnumGender;
import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.business.AuditoriaBusiness;
import anubis.generic.business.GenericBusiness;
import anubis.generic.dao.GenericDAO;
import anubis.generic.dto.QueryDataTablesDTO;
import anubis.generic.dto.SearchDTO;
import anubis.generic.dto.SimpleGenericDTO;
import anubis.generic.security.AnubisUserSecurity;
import anubis.response.Response;
import anubis.response.ResponseException;
import anubis.validator.SqlInjectionValidator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController("genericController")
public abstract class GenericController<Gbean extends SimpleGenericBean, Gdto extends SimpleGenericDTO<Gbean>, Gdao extends GenericDAO<Gbean>, Gbus extends GenericBusiness<Gbean, Gdao>> {

	@Autowired
	protected Gbus business;
	
	@Autowired
	private AuditoriaBusiness auditoriaBusiness;
	
	@Autowired
	private SqlInjectionValidator sqlInjectionValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if(binder.getTarget() != null && this.sqlInjectionValidator.supports(binder.getTarget().getClass())){
            binder.setValidator(this.sqlInjectionValidator);
        }
	}
	
	protected String getOrderBy() {
		return "nome";
	}
	
	protected Gdto populateDTO(Gdto dto) {
		return dto;
	}
	
	protected Long getUsuarioLogado() {
		Object objectUsuario = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (objectUsuario == null || objectUsuario.equals("anonymousUser")) {
			return 0l;
		}
		return ((AnubisUserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	}
	
	//##################################### INSERT #####################################
	
	protected void completeInsert(Gdto dto, Gbean bean) {
		checkConverterFields(dto, bean, bean.getClass(), dto.getClass());
	}
	
	@CacheEvict(value = "publicLists", allEntries = true)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(rollbackFor = {Exception.class, Throwable.class}, timeout = 120, propagation = Propagation.NESTED)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Response insert(@RequestBody @Valid Gdto dto) throws Exception {
		Gbean bean = dto.converter();
		completeInsert(dto, bean);
		business.insert(bean);
		SimpleGenericDTO newDto = populateDTO((Gdto) business.getDTO(bean.getId()));
		saveAuditoriaInsert(newDto);
		Response response = successMessageInsert(bean);
		response.setResponse(newDto);
		return response;
	}
	
	protected Response successMessageInsert(Gbean bean) {
		String mensagem = business.isMale(bean) ? "crud.generic.cadastrado.sucesso.masculino" : "crud.generic.cadastrado.sucesso.feminino";
		return Response.returnMessage(mensagem, business.getLabelBean(bean));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void saveAuditoriaInsert(SimpleGenericDTO dto) throws ResponseException {
		try {
			Class classe = dto.getClass();
			AuditoriaOperations operation = (AuditoriaOperations) classe.getAnnotation(AuditoriaOperations.class);
			if(operation != null) {
				auditoriaBusiness.inserirAuditoria(operation.insert(), null, dto, dto.getId(), getUsuarioLogado(), business.getSlugBean());
			}
		} catch (Exception e) {
			throw new ResponseException(e, "auditoria.erro", "label.cadastro");
		}
	}
	
	
	//##################################### UPDATE #####################################
	
	protected void completeUpdate(Gdto dto, Gbean bean, Gbean beanOld) {
		checkConverterFields(dto, bean, bean.getClass(), dto.getClass());
	}

	@CacheEvict(value = "publicLists", allEntries = true)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = {Exception.class, Throwable.class}, timeout = 120, propagation = Propagation.NESTED)
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Response update(@PathVariable("id") Long id, @RequestBody @Valid Gdto dto) throws Exception {
		SimpleGenericDTO dtoOld = populateDTO((Gdto) business.getDTO(id));
		Gbean bean = business.getById(id);
		Gbean beanOld = (Gbean) bean.clone();
		dto.setId(id);
		dto.converter(bean);
		completeUpdate(dto, bean, beanOld);
		bean = business.edit(bean, beanOld);
		dto = (Gdto) business.getDTO(bean.getId());
		dto = populateDTO(dto);
		saveAuditoriaUpdate(dto, dtoOld);
		Response response = successMessageUpdate(bean);
		response.setResponse(dto);
		return response;
	}
	
	protected Response successMessageUpdate(Gbean bean) {
		String mensagem = business.isMale(bean) ? "crud.generic.alterado.sucesso.masculino" : "crud.generic.alterado.sucesso.feminino";
		return Response.returnMessage(mensagem, business.getLabelBean(bean));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void saveAuditoriaUpdate(SimpleGenericDTO simpleDto, SimpleGenericDTO dtoOld) {
		try {
			Class classe = simpleDto.getClass();
			AuditoriaOperations operation = (AuditoriaOperations) classe.getAnnotation(AuditoriaOperations.class);
			if(operation != null) {
				SimpleGenericDTO dto = simpleDto.clone();
				for(Field field : dto.getClass().getDeclaredFields()) {
					IgnoreAuditoria annotation = field.getAnnotation(IgnoreAuditoria.class);
					if(annotation != null) {
						field.setAccessible(true);
						field.set(dto, null);
						field.setAccessible(false);
					}
				}
				auditoriaBusiness.inserirAuditoria(operation.update(), dtoOld, dto, dto.getId(), getUsuarioLogado(), business.getSlugBean());
			}
		} catch (Exception e) {
			throw new ResponseException(e, "auditoria.erro", "label.edicao");
		}
	}
	
	
	//##################################### DELETE #####################################

	@CacheEvict(value = "publicLists", allEntries = true)
	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor = {Exception.class, Throwable.class}, timeout = 120)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Response delete(@PathVariable("id") Long id) throws Exception {
		Gbean bean = business.getById(id);
		SimpleGenericDTO dto = business.getDTO(id);
		business.remove(bean);
		saveAuditoriaDelete(dto);
		Response response = successMessageDelete(bean);
		response.setResponse(dto);
		return response;
	}

	protected Response successMessageDelete(Gbean bean) {
		String mensagem = business.isMale(bean) ? "crud.generic.excluido.sucesso.masculino" : "crud.generic.excluido.sucesso.feminino";
		return Response.returnMessage(mensagem, business.getLabelBean(bean));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void saveAuditoriaDelete(SimpleGenericDTO dto) {
		try {
			Class classe = dto.getClass();
			AuditoriaOperations operation = (AuditoriaOperations) classe.getAnnotation(AuditoriaOperations.class);
			if(operation != null) {
				auditoriaBusiness.inserirAuditoria(operation.delete(), dto, null, dto.getId(), getUsuarioLogado(), business.getSlugBean());
			}
		} catch (Exception e) {
			throw new ResponseException(e, "auditoria.erro", "label.exclusao");
		}
	}
	
	
	//##################################### LIST #####################################
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Response getById(@PathVariable("id") Long id) {
		Gdto dto = (Gdto) business.getDTO(id);
		return Response.returnObject(populateDTO(dto));
	}
	
	@Transactional(readOnly=true)
	@RequestMapping(value = "/pagination", method = RequestMethod.POST)
	public Map<String, Object> pagination(@RequestBody QueryDataTablesDTO query) {
		return business.pagination(query);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Transactional(readOnly=true)
	public Response search(@RequestBody @Valid SearchDTO search) {
		return Response.returnObject(business.search(search.getField(), search.getQuery(), search.getCountRecord()));
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Response listAll() {
		return Response.returnObject(business.listAllDTO(null, getOrderBy()), "crud.generic.list", business.getLabelBean(null));
	}

	@RequestMapping(value = "/ativo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Response listAllAtivo() {
		return Response.returnObject(business.listAllDTO(true, getOrderBy()), "crud.generic.list", business.getLabelBean(null));
	}

	@RequestMapping(value = "/active", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Response listAllActives() {
		return Response.returnObject(business.listAllDTO(true, getOrderBy()), "crud.generic.list", business.getLabelBean(null));
	}

	@RequestMapping(value = "/basic", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Response listSimpleDTO() {
		return Response.returnObject(business.listSimpleDTO(true, getOrderBy()), "crud.generic.list", business.getLabelBean(null));
	}
	

	public void checkConverterFields(Gdto dto, Gbean bean, Class<?> classeBean, Class<?> classeDTO) { 
		if(!classeDTO.getSuperclass().getName().equals("java.lang.Object")) {
			checkConverterFields(dto, bean, bean.getClass().getSuperclass(), classeDTO.getSuperclass());
		} else {
			return;
		}
		
		String fieldName = null;
		try {
			for(Field field : classeDTO.getDeclaredFields()) {
				fieldName = field.getName();
				if(field.isAnnotationPresent(BeanConverter.class)) {
					callBeanConverter(field, dto, bean, classeBean, classeDTO);
				} else if(field.isAnnotationPresent(ListBeanConverter.class)) {
					callListBeanConverter(field, dto, bean);
				}
			}
		} 
		catch (NoSuchFieldException e) {
			throw new ResponseException("Convert DTO to Bean error: No such field: " + fieldName);
		} catch (ResponseException e) {
			throw new ResponseException(e.getMessage() + ":" + fieldName);
		}
	}
	
	public void callBeanConverter(Field field, SimpleGenericDTO<? extends SimpleGenericBean> dto, SimpleGenericBean bean, Class<?> classeBean, Class<?> classeDTO) throws SecurityException {
		Field fieldBean = null;
		try {
			field.setAccessible(true);
			BeanConverter beanConverter = field.getAnnotation(BeanConverter.class);
			Object idBean = field.get(dto);
			String fieldBeanName = beanConverter.field();
			fieldBean = getFieldBean(fieldBeanName, classeBean);
			fieldBean.setAccessible(true);
			if(idBean != null) {
				Class<?> clazz = beanConverter.bean();
				Object resultado = business.getDAO().findById(idBean, clazz);
				if(resultado == null) {
					BeanProperties beanProperties = classeBean.getAnnotation(BeanProperties.class);
					boolean isMale = beanProperties.gender().equals(EnumGender.MALE);
					
					String message = isMale ? "crud.generic.nao.encontrado.masculino" : "crud.generic.nao.encontrado.feminino";
					throw new ResponseException(message, beanProperties.label());
				}
				fieldBean.set(bean, resultado);
			} else {
				fieldBean.set(bean, null);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			field.setAccessible(false);
			if(fieldBean != null) {
				fieldBean.setAccessible(false);
			}
		}
	}
	
	public Field getFieldBean(String fieldBeanName, Class<?> classeBean) {
		Field field = null;
		try {
			field = classeBean.getDeclaredField(fieldBeanName);
		} catch (NoSuchFieldException e) {
			if(!classeBean.getSuperclass().getName().equals("java.lang.Object")) {
				field = getFieldBean(fieldBeanName, classeBean.getSuperclass());
			} else {
				throw new ResponseException("Convert DTO to Bean error: No such field: " + fieldBeanName);
			}
		}
		return field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void callListBeanConverter(Field field, SimpleGenericDTO<? extends SimpleGenericBean> dto, SimpleGenericBean bean) {
		Field fieldBean = null;
		Field rootBeanField = null;
		try {
			field.setAccessible(true);
			Collection<? extends SimpleGenericDTO> listaDTO = (Collection<SimpleGenericDTO>) field.get(dto);
			if(listaDTO != null) {
				ListBeanConverter listBeanConverter = field.getAnnotation(ListBeanConverter.class);
				fieldBean = bean.getClass().getDeclaredField(listBeanConverter.field());
				fieldBean.setAccessible(true);
				
				if(fieldBean.get(bean) == null) {
					if(Set.class.isAssignableFrom(fieldBean.getType())) {
						fieldBean.set(bean, new HashSet<Object>());
					} else if(List.class.isAssignableFrom(fieldBean.getType())) {
						fieldBean.set(bean, new ArrayList<Object>());
					} else {
						throw new ResponseException("Erro ao criar coleção de objetos");
					}
				}
				Collection<Object> listaBean = ((Collection<Object>) fieldBean.get(bean));
				listaBean.size();
				listaBean.clear();
				
				if(listaDTO != null && !listaDTO.isEmpty()) {
					SimpleGenericBean itemAux = listaDTO.iterator().next().converter();
					rootBeanField = itemAux.getClass().getDeclaredField(listBeanConverter.referecedBy());
					if(rootBeanField == null) {
						throw new ResponseException("Não foi encontrado o campo root que gerencia a lista de " + field.getName());
					}
					rootBeanField.setAccessible(true);
					
					for(SimpleGenericDTO itemDTO : listaDTO) {
						SimpleGenericBean itemBean = itemDTO.converter();
						rootBeanField.set(itemBean, bean);
						callBeanConverter(itemDTO, itemBean);
						listaBean.add(itemBean);
					}
				}
			}
		} catch(NoSuchFieldException e){
			// Se o field não existir, passe para o próximo field com a annotation @ListBeanConverter
		} catch (Exception e) {
			throw new ResponseException("Ocorreu um erro ao converter os itens da lista de DTO");
		} finally {
			field.setAccessible(false);
			if(fieldBean != null) {
				fieldBean.setAccessible(false);
			}
			if(rootBeanField != null) {
				rootBeanField.setAccessible(false);
			}
		}
	}
	
	private void callBeanConverter(SimpleGenericDTO<? extends SimpleGenericBean> dto, SimpleGenericBean bean) throws NoSuchFieldException, SecurityException {
		String fieldName = null;
		try {
			Class<?> classeDTO = dto.getClass();
			for(Field field : classeDTO.getDeclaredFields()) {
				fieldName = field.getName();
				if(field.isAnnotationPresent(BeanConverter.class)) {
					callBeanConverter(field, dto, bean, bean.getClass(), dto.getClass());
				}
			}
		} catch (ResponseException e) {
			throw new ResponseException(e.getMessage() + ":" + fieldName);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/skeleton", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Response getSkeleton() throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Class<Gdto> entityClass = (Class<Gdto>) genericSuperclass.getActualTypeArguments()[1];
		SimpleGenericDTO dto = entityClass.newInstance();
		return Response.returnObject(dto, "skeleton");
	}
	
}
