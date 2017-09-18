package anubis.generic.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import anubis.generic.bean.ConfigurationGenericBean;
import anubis.generic.dao.GenericDAO;
import anubis.generic.dto.ConfigurationGenericDTO;
import anubis.utils.test.ControllerIntegrationTestEnum;
import anubis.utils.test.EnumStatusResponseTest;

@Component("configurationTestGeneric")
public abstract class ConfigurationTestGeneric<Gbean extends ConfigurationGenericBean, Gdto extends ConfigurationGenericDTO<Gbean>, Gdao extends GenericDAO<Gbean>, Gen extends GeneratorGeneric<Gbean, Gdto>> extends SimpleTestGeneric<Gbean, Gdto, Gdao, Gen> {

	@Autowired
	protected Gen generator;

	protected char quotes ='"';
	
	protected abstract void buildScenarioRegisterWithDependences(Gbean bean) throws Exception ;

	@Override
	public Gen getGenerator() {
		return generator;
	}
	
	@Test
	public void createWithFieldNameEmpty() throws Exception {
		Gdto dto = generator.newRegistro("CREATE-WITH-FIELD-NAME-EMPTY");
		dto.setNome(null);
		this.gDto = dto;
		
		Map<String, Object> map = new HashMap<>();
		map.put(ControllerIntegrationTestEnum.STATUS.getValor(), EnumStatusResponseTest.ERROR.getValor());
		map.put(ControllerIntegrationTestEnum.MESSAGE.getValor(), "validacao");
		map.put("errors[0].campo", "nome");
		map.put("errors[0].mensagem", EnumMessagesTest.CREATE_EDIT_FIELD_NAME_EMPTY.getMessage(null, null, null, null));
		callMethodControllerPostValidator(map, dto, "/");
	}
	
	@Test
	public void createWithFieldActiveEmpty() throws Exception {
		Gdto dto = generator.newRegistro("CREATE-WITH-FIELD-ACTIVE");
		dto.setIsAtivo(null);
		this.gDto = dto;
		
		Map<String, Object> map = new HashMap<>();
		map.put(ControllerIntegrationTestEnum.STATUS.getValor(), EnumStatusResponseTest.ERROR.getValor());
		map.put(ControllerIntegrationTestEnum.MESSAGE.getValor(), "validacao");
		map.put("errors[0].campo", "isAtivo");
		map.put("errors[0].mensagem", EnumMessagesTest.CREATE_EDIT_FIELD_ACTIVE_EMPTY.getMessage(null, null, null, null));
		callMethodControllerPostValidator(map, dto, "/");
	}
	
	@Test
	public void createUniqueFieldExist() throws Exception {
		Gdto dto1 = generator.newRegistro("CREATE-UNIQUE-FIELD-EXIST");
		this.gDto = dto1;
		Gbean bean1 = create(dto1);
		dtoCreateUniqueInterceptor(dto1);
		callMethodControllerPost(dto1, EnumStatusResponseTest.ERROR, EnumMessagesTest.CREATE_EDIT_UNIQUE_FIELD_EXIST.getMessage(isMasculino(), getAttributeUniqueField(), getValueAttributeUniqueField(bean1), getNomeController()), "");
	}
	
	protected void dtoCreateUniqueInterceptor(Gdto dto) { }
	
	@Test
	public void editWithFieldNameEmpty() throws Exception {
		Gdto dto = generator.newRegistro("EDIT-WITH-FIELD-NAME-EMPTY");
		this.gDto = dto;
		Gbean bean = create(dto);
		Gdto dtoEdit = generator.newRegistro("EDIT-WITH-FIELD-NAME-EMPTY");
		dtoEdit.setNome(null);
		
		Map<String, Object> map = new HashMap<>();
		map.put(ControllerIntegrationTestEnum.STATUS.getValor(), EnumStatusResponseTest.ERROR.getValor());
		map.put(ControllerIntegrationTestEnum.MESSAGE.getValor(), "validacao");
		map.put("errors[0].campo", "nome");
		map.put("errors[0].mensagem", EnumMessagesTest.CREATE_EDIT_FIELD_NAME_EMPTY.getMessage(null, null, null, null));
		callMethodControllerPostValidator(map, dtoEdit, "/{id}", bean.getId());
	}
	
	@Test
	public void editWithFieldActiveEmpty() throws Exception {
		Gdto dto = generator.newRegistro("EDIT-WITH-FIELD-ACTIVE-EMPTY");
		this.gDto = dto;
		Gbean bean = create(dto);
		Gdto dtoEdit = generator.newRegistro("EDIT-WITH-FIELD-ACTIVE-EMPTY");
		dtoEdit.setIsAtivo(null);
		
		Map<String, Object> map = new HashMap<>();
		map.put(ControllerIntegrationTestEnum.STATUS.getValor(), EnumStatusResponseTest.ERROR.getValor());
		map.put(ControllerIntegrationTestEnum.MESSAGE.getValor(), "validacao");
		map.put("errors[0].campo", "isAtivo");
		map.put("errors[0].mensagem", EnumMessagesTest.CREATE_EDIT_FIELD_ACTIVE_EMPTY.getMessage(null, null, null, null));
		callMethodControllerPostValidator(map, dtoEdit, "/{id}", bean.getId());
	}
	
	@Test
	public void editUniqueFieldExist() throws Exception {
		Gdto dto1 = generator.newRegistro("EDIT-UNIQUE-FIELD-EXIST");
		this.gDto = dto1;
		Gbean bean1 = create(dto1);
		Gdto dto2 = generator.newRegistro("EDIT-UNIQUE-FIELD-EXIST");
		Gbean bean2 = create(dto2);
		
		dtoEditUniqueInterceptor(dto1);
		
		callMethodControllerPost(dto1, EnumStatusResponseTest.ERROR, EnumMessagesTest.CREATE_EDIT_UNIQUE_FIELD_EXIST.getMessage(isMasculino(), getAttributeUniqueField(), getValueAttributeUniqueField(bean1), getNomeController()), "/{id}", bean2.getId());
	}
	
	protected void dtoEditUniqueInterceptor(Gdto dto) { }
	
	@Test
	public void deleteWithDependences() throws Exception {
		Gdto dto = generator.newRegistro("DELETE-WITH-DEPENDENCES");
		this.gDto = dto;
		Gbean bean = create(dto);
		buildScenarioRegisterWithDependences(bean);
		
		callMethodControllerDelete(EnumStatusResponseTest.ERROR, EnumMessagesTest.DELETE_WITH_DEPENDENCIES.getMessage(isMasculino(), null, null, getNomeController()), "/{id}", bean.getId());
	}
	
	@Test
	public void listActives() throws Exception {
		Map<Long, Gbean> initialRecords = initialRecords();
		List<Long> inectiveIds = new ArrayList<Long>();
		Set<Long> ids = initialRecords.keySet();
		for(Long id : ids) {
			if(!initialRecords.get(id).getIsAtivo()) {
				inectiveIds.add(id);
			}
		}
		for(Long id : inectiveIds) {
			initialRecords.remove(id);
		}
		testList(initialRecords, "/ativo");
	}
	
	public List<Gbean> getListAll(boolean isAtivo, String orderBy) {
		return dao.listAll(isAtivo, orderBy);
	}
	
	protected Map<Long, Gbean> initialRecords() throws Exception {
		Map<Long, Gbean> initialRecords = new HashMap<>();
		
		Gdto dto1 = generator.newRegistro("INITIAL-RECORDS");
		Gdto dto2 = generator.newRegistro("INITIAL-RECORDS");
		dto2.setIsAtivo(false);
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
