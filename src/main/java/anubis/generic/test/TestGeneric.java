package anubis.generic.test;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import anubis.annotation.BeanProperties;
import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dao.GenericDAO;
import anubis.generic.dto.SimpleGenericDTO;
import anubis.response.Response;

@Component("testGeneric")
public abstract class TestGeneric<Gbean extends SimpleGenericBean, Gdto extends SimpleGenericDTO<Gbean>, Gdao extends GenericDAO<Gbean>> extends ApplicationTest {

	@Autowired
	protected Gdao dao;
	
	protected Gbean gBean;
	
	protected Gdto gDto;
	
	@Override
	public boolean isMasculino() {
		BeanProperties properties = getClazz().getAnnotation(BeanProperties.class);
		return properties.gender().isMale();
    }
	
    @SuppressWarnings("unchecked")
  	protected Class<Gbean> getClazz() {
  		return (Class<Gbean>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  	}
	
	@SuppressWarnings("unchecked")
	private Long getId(MvcResult result) throws JsonSyntaxException, UnsupportedEncodingException {
		Response response = new Gson().fromJson(result.getResponse().getContentAsString(), Response.class);
		LinkedTreeMap<String, ?> dto = (LinkedTreeMap<String, ?>) response.getResponse();
		return ((Double) dto.get("id")).longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private List getList(MvcResult result) throws JsonSyntaxException, UnsupportedEncodingException {
		Response response = new Gson().fromJson(result.getResponse().getContentAsString(), Response.class);
		List listDto = (List) response.getResponse();
		return listDto;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Gbean getBeanById(Integer id) {
		return dao.findById(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<Gbean> getListAll() {
		return dao.listAll(null, null);
	}
	
	public Long getIdNotFound() {
		Long id = dao.maxId();
		if(id != null){
			id = dao.maxId() + 255;
		} else {
			id = 255l;
		}
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public Gdto get(Long id) throws Exception {
		ResultActions resultAction = getMockMVC().perform(MockMvcRequestBuilders.get(URL_API + getSlug() + "/{id}", id)
				.characterEncoding(UTF_8)
				.locale(getLocale())
				.contentType(contentType));
		processResponseSuccessResultTest(resultAction);
		MvcResult result = resultAction
				.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath(STATUS).value(containsString(SUCCESS)))
				.andReturn();
		
		Response response = new Gson().fromJson(result.getResponse().getContentAsString(), Response.class);
		Class<Gdto> dtoClass = (Class<Gdto>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		
		return new Gson().fromJson(new Gson().toJson(((LinkedTreeMap<String, Object>) response.getResponse())), dtoClass);
	}
	
	
	public Gbean create(Gdto dto) throws Exception {
		return create(dto, "");
	}
	
	public Gbean create(Gdto dto, String url, Object... params) throws Exception {
		this.gDto = dto;
		MvcResult result = callMethodController(false, null, dto, HttpStatus.OK, HttpMethod.POST, EnumMessagesTest.CREATE.getMessage(isMasculino(), null, null, getNomeController()), url, params);
//		processResponseSuccessResultTest(resultAction);
		this.gBean = dao.findById(getId(result)); 
		return this.gBean;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Gbean> create(List<Gdto> listDto, String url, Object... params) throws Exception {
		MvcResult result = callMethodController(false, null, listDto, HttpStatus.OK, HttpMethod.POST, EnumMessagesTest.CREATE.getMessage(isMasculino(), null, null, getNomeController()), url, params);
//		processResponseSuccessResultTest(resultAction);
		List listReturn = getList(result);
		List<Gbean> listBean = new ArrayList<>();
		for(int i=0; i<listReturn.size(); i++) {
			LinkedTreeMap<String, Object> linked = (LinkedTreeMap<String, Object>) listReturn.get(i);
			Gbean bean = dao.findById(((Double) linked.get("id")).longValue());
			listBean.add(bean);
		}
		Assert.assertTrue(listDto.size() == listReturn.size());
		return listBean;
	}
	
	public Gbean edit(Gdto dto) throws Exception {
		return edit(dto, "/{id}", dto.getId());
	}
	
	public Gbean edit(Gdto dto, String url, Object... params) throws Exception {
		this.gDto = dto;
		MvcResult result = callMethodController(false, null, dto, HttpStatus.OK, HttpMethod.POST, EnumMessagesTest.EDIT.getMessage(isMasculino(), null, null, getNomeController()), url, params);
//		processResponseSuccessResultTest(resultAction);
		return dao.findById(getId(result));
	}
	
	
	public void delete(Long id) throws Exception {
		delete("/{id}", id);
	}
	
	public void delete(String url, Object... params) throws Exception {
		callMethodController(false, null, null, HttpStatus.OK, HttpMethod.DELETE, EnumMessagesTest.DELETE.getMessage(isMasculino(), null, null, getNomeController()), url, params);
//		processResponseSuccessResultTest(resultAction);
	}
	
	public String deleteStatus() {
		return SUCCESS;
	}
	
}
