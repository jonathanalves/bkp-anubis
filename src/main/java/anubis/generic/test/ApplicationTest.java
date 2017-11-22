package anubis.generic.test;

import static org.hamcrest.CoreMatchers.containsString;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import anubis.enumeration.EnumStatusRetorno;
import anubis.generic.business.LoginBusiness;
import anubis.generic.security.AnubisUserSecurity;
import anubis.response.Response;
import anubis.utils.test.ControllerIntegrationTestEnum;
import anubis.utils.test.EnumStatusResponseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest("applicationTest")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class ApplicationTest {
	
	protected static final boolean ativateLogResponse = true;
	
	public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
	protected static final String ERROR = "ERROR";
	protected static final String MESSAGE = "message";
	protected static final String STATUS = "status";
	protected static final String SUCCESS = "SUCCESS";
	public static final String URL_API = "http://localhost:8080/api/";
	protected static final String URL_APP = "http://localhost:8080/app/";
	public static final String UTF_8 = "UTF-8";
	
	public static MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName(UTF_8));
	
	protected MockMvc mockMvc;
	
    @Autowired
    protected WebApplicationContext webApplicationContext;
    
    @Autowired
    protected GeneratorValues generatorValues;
    
    @SuppressWarnings("rawtypes")
	@Autowired
    protected LoginBusiness loginBusiness;
    
    public boolean isMasculino() {
    	return true;
    }
    
    public String getSlug() {
    	return "slug";
    }
    
    public Locale getLocale() {
    	return Locale.getDefault();
    }
    
    public String getNomeController() {
    	return "controller";
    }
    
    protected abstract String getLogin();

    @Before
	public void setup() throws Exception {
    	System.out.println("INICIANDO TESTE ------------------------------------------------------- ");
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    	login(getLogin());
	}
    
    @After
	public void teardown() {
	    SecurityContextHolder.clearContext();
    	System.out.println("FECHANDO TESTE ------------------------------------------------------- ");
	}

    public MockMvc getMockMVC() {
    	if(mockMvc == null) {
    		mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    	}
    	return mockMvc;
    }
    
	protected void login(String login) throws Exception {
		AnubisUserSecurity anubisUserSecurity = loginBusiness.login(login);
		SecurityContextHolder.getContext().setAuthentication(new UserSecurityTest(anubisUserSecurity));
	}
    
	public void callMethodControllerPostContains(EnumStatusResponseTest status, boolean useContains, String message, String url, Object... args) throws Exception{
		callMethodController(useContains, null, null, getHttpStatus(status), HttpMethod.POST, message, url, args);
	}
	
    public void callMethodControllerPost(Object content, EnumStatusResponseTest status, String url, Object... args) throws Exception{
		callMethodController(false, null, content, getHttpStatus(status), HttpMethod.POST, null, url, args);
	}
    
    public void callMethodControllerPost(EnumStatusResponseTest status, String url, Object... args) throws Exception{
		callMethodController(false, null, null, getHttpStatus(status), HttpMethod.POST, null, url, args);
	}
	
    
	public void callMethodControllerPost(Object content, EnumStatusResponseTest status, String message, String url, Object... args) throws Exception{
		callMethodController(false, null, content, getHttpStatus(status), HttpMethod.POST, message, url, args);
	}
	
	public void callMethodControllerPost(Object content, boolean useContains, EnumStatusResponseTest status, String message, String url, Object... args) throws Exception{
		callMethodController(useContains, null, content, getHttpStatus(status), HttpMethod.POST, message, url, args);
	}
	
	public void callMethodControllerPost(EnumStatusResponseTest status, String message, String url, Object... args) throws Exception{
		callMethodController(false, null, null, getHttpStatus(status), HttpMethod.POST, message, url, args);
	}
	
	public void callMethodControllerPostValidator(Map<String, Object> map, Object content, String url, Object... args) throws Exception{
		String message = map.get(ControllerIntegrationTestEnum.MESSAGE.getValor()).toString();
		HttpStatus httpStatus = null;
		if(map.get(ControllerIntegrationTestEnum.STATUS.getValor()).toString().equals(EnumStatusResponseTest.SUCCESS.getValor())) {
			httpStatus = HttpStatus.OK;
		}else if(map.get(ControllerIntegrationTestEnum.STATUS.getValor()).toString().equals(EnumStatusResponseTest.ERROR.getValor())) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		callMethodController(false, map, content, httpStatus, HttpMethod.POST, message, url, args);
	}
	
	public void callMethodControllerGet(EnumStatusResponseTest status, String message, String url, Object... args) throws Exception{
		callMethodController(false, null, null, getHttpStatus(status), HttpMethod.GET, message, url, args);
	}
	
	public void callMethodControllerGet(Map<String, Object> map, EnumStatusResponseTest status, String message, String url, Object... args) throws Exception{
		callMethodController(false, null, null, getHttpStatus(status), HttpMethod.GET, message, url, args);
	}
	
	public void callMethodControllerDelete(EnumStatusResponseTest status, String message, String url, Object... args) throws Exception {
		callMethodController(false, null, null, getHttpStatus(status), HttpMethod.DELETE, message, url, args);
	}
    
	protected void processResponseSuccessResultTest(ResultActions resultAction) throws Exception {
		if(ativateLogResponse){
			MvcResult result = resultAction.andReturn();
			Response response = new Gson().fromJson(result.getResponse().getContentAsString(), Response.class);
			if(!EnumStatusRetorno.SUCCESS.equals(response.getStatus())) {
				//FIXME PHILIPE ARRUMAR
//				OdinLogger.info("<<ERROR ODIN TEST>>");
				resultAction.andDo(MockMvcResultHandlers.print());
			}
		}
	}

	public MvcResult callMethodController(boolean useContains, Map<String, Object> map, Object content, HttpStatus status, HttpMethod method, String message, String url, Object... args) throws Exception {
		String slug = (url.startsWith("#/")) ? "" : getSlug();
		url = url.replace("#", "");
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(method, URL_API + slug + url, args)
		.characterEncoding(UTF_8)
		.locale(getLocale())
		.contentType(contentType);
		
		if(content != null) {
			builder.content(new Gson().toJson(content == null ? "" : content));
		}
		
		ResultActions resultActions = getMockMVC().perform(builder);
		
		if(ativateLogResponse) {
			resultActions.andDo(MockMvcResultHandlers.print());
		}
		
		resultActions.andExpect(MockMvcResultMatchers.content().encoding(UTF_8));
		resultActions.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_CHARSET_UTF_8));
		

		resultActions.andExpect(MockMvcResultMatchers.status().is(status.value()));
		
		if(map != null) {
			Set<String> chaves = map.keySet();
			for (String chave : chaves) {
				if(map.get(chave) instanceof String){
					resultActions.andExpect(MockMvcResultMatchers.jsonPath(chave).value(containsString(map.get(chave).toString())));
				} else {
					resultActions.andExpect(MockMvcResultMatchers.jsonPath(chave).value(map.get(chave)));
				}
			}
		}
		
		if(message != null) {
			if(useContains) {
				resultActions.andExpect(MockMvcResultMatchers.jsonPath(MESSAGE).value(containsString(message)));
			}else {
				resultActions.andExpect(MockMvcResultMatchers.jsonPath(MESSAGE).value(message));
			}
		}
		
		return resultActions.andReturn();
	}
	
	public HttpStatus getHttpStatus(EnumStatusResponseTest status) {
		HttpStatus httpStatus;
		if(status.equals(EnumStatusResponseTest.ERROR)) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}else {
			httpStatus = HttpStatus.OK;
		}
		return httpStatus;
	}
}
