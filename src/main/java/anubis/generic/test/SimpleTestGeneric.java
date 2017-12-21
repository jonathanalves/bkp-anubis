package anubis.generic.test;

import anubis.MessageSystem;
import anubis.annotation.BeanProperties;
import anubis.generic.bean.ConfigurationGenericBean;
import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dao.GenericDAO;
import anubis.generic.dto.SimpleGenericDTO;
import anubis.response.Response;
import anubis.utils.test.EnumStatusResponseTest;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("simpleTestGeneric")
public abstract class SimpleTestGeneric<Gbean extends SimpleGenericBean, Gdto extends SimpleGenericDTO<Gbean>, Gdao extends GenericDAO<Gbean>, Gen extends GeneratorGeneric<Gbean, Gdto>> extends TestGeneric<Gbean, Gdto, Gdao>{

	@Autowired
	protected Gen generator;
	
	public Gen getGenerator() {
		return generator;
	}
    
    public String getSlug() {
    	BeanProperties properties = getClazz().getAnnotation(BeanProperties.class);
		return properties.slug();
    }
    
    public String getNomeController() {
    	BeanProperties properties = getClazz().getAnnotation(BeanProperties.class);
		return MessageSystem.getMessage(properties.label());
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
  
    @Test
	public void get() throws Exception {
		Gdto dtoCadastro = generator.newRegistro("GET");
		this.gDto = dtoCadastro;
		this.gBean = create(dtoCadastro);
		Map<String, Object> map = new HashMap<>();
		map.put("response.id", this.gBean.getId());
		callMethodController(false, map, null, HttpStatus.OK, HttpMethod.GET, null, "/{id}", this.gBean.getId());
	}
	
	@Test
	public void getNotFound() throws Exception {
		this.gBean = dao.findById(getIdNotFound());
		callMethodControllerGet(EnumStatusResponseTest.ERROR, EnumMessagesTest.GET_EDIT_DELETE_NOT_FOUND.getMessage(isMasculino(), null, null, getNomeController()), "/{id}", getIdNotFound());
	}
	
	@Test
	public void create() throws Exception {
		create("CREATE");
	}
	
	@Test
	public void edit() throws Exception {
		Gdto dtoCadastro = generator.newRegistro("EDIT");
		this.gDto = dtoCadastro;
		this.gBean = create(dtoCadastro);
		Gdto dtoEdicao = generator.newRegistro("EDIT");
		this.gDto = dtoEdicao;
		dtoEdicao = generator.editRegistro(this.gBean, dtoEdicao);
		edit(dtoEdicao);
	}
	
	@Test
	public void editNotFound() throws Exception {
		Gdto dto = generator.newRegistro("EDIT-NOT-FOUND");
		this.gDto = dto;
		callMethodControllerPost(dto, EnumStatusResponseTest.ERROR, EnumMessagesTest.GET_EDIT_DELETE_NOT_FOUND.getMessage(isMasculino(), null, null, getNomeController()), "/{id}", getIdNotFound());
	}
	
	@Test
	public void delete() throws Exception {
		Gdto dto = generator.newRegistro("DELETE");
		this.gDto = dto;
		this.gBean = create(dto);
		delete(this.gBean.getId());
	}
	
	@Test
	public void deleteNotFound() throws Exception {
		this.gBean = dao.findById(getIdNotFound());
		callMethodControllerDelete(EnumStatusResponseTest.ERROR, EnumMessagesTest.GET_EDIT_DELETE_NOT_FOUND.getMessage(isMasculino(), null, null, getNomeController()), "/{id}", getIdNotFound());
	}
	
	@Test
	public void list() throws Exception {
		Map<Long, Gbean> initialRecords = initialRecords();
		testList(initialRecords, "");
	}
	
	
	
	public Gbean create(String metodo) throws Exception {
		Gdto dto = generator.newRegistro(metodo);
		this.gDto = dto;
		return create(dto);
	}
	
	@SuppressWarnings("unchecked")
	protected void testList(Map<Long, Gbean> result, String slug, Object... objects) throws Exception {
		MvcResult testResult = callMethodController(false, null, null, HttpStatus.OK, HttpMethod.GET, null, slug, objects);
		Response response = new Gson().fromJson(testResult.getResponse().getContentAsString(), Response.class);
		List<LinkedTreeMap<String,?>> listaResponse = (List<LinkedTreeMap<String,?>>) response.getResponse();
		Integer count = 0;
		for(int i=0; i<listaResponse.size(); i++) {
			Long id = ((Double) listaResponse.get(i).get("id")).longValue();
			Gbean bean = result.get(id);
			if(bean != null) {
				count++;
			}
		}
		boolean isTrue = count.intValue()==result.size();
		Assert.assertTrue("Verificação de listagem", isTrue);
	}
	
	@SuppressWarnings("unchecked")
	protected void testListHelp(Map<Long, String> result, String atributo, String message, String slug, Object... objects) throws Exception {
		MvcResult testResult = callMethodController(false, null, null, HttpStatus.OK, HttpMethod.GET, message, slug, objects);
		Response response = new Gson().fromJson(testResult.getResponse().getContentAsString(), Response.class);
		List<LinkedTreeMap<String,?>> listResponse = (List<LinkedTreeMap<String,?>>) response.getResponse();
		Integer count = 0;
		for(int i=0; i<listResponse.size(); i++) {
			Long id = ((Double) listResponse.get(i).get("id")).longValue();
			String nomeRetornado = listResponse.get(i).get(atributo).toString();
			String nomeLista = result.get(id);
			if(nomeLista != null && nomeLista.equals(nomeRetornado)) {
				count++;
			}
		}
		boolean isTrue = count.intValue()==result.size();
		Assert.assertTrue("Verificação de listagem", isTrue);
	}
	
	protected Map<Long, Gbean> initialRecords() throws Exception {
		Map<Long, Gbean> initialRecords = new HashMap<>();
		
		Gdto dto1 = generator.newRegistro("INITIAL-RECORDS");
		Gdto dto2 = generator.newRegistro("INITIAL-RECORDS");
		Gdto dto3 = generator.newRegistro("INITIAL-RECORDS");
		
		this.gDto = dto1;
		Gbean bean1 = create(dto1);
		this.gDto = dto2;
		Gbean bean2 = create(dto2);
		this.gDto = dto3;
		Gbean bean3 = create(dto3);
		
		initialRecords.put(bean1.getId(), bean1);
		initialRecords.put(bean2.getId(), bean2);
		initialRecords.put(bean3.getId(), bean3);
		
		return initialRecords;
	}
	
}
