package anubis;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration("messageSystem")
public class MessageSystem {
	
	@Cacheable 
	public static String getMessage(String message, String... arg){
		String messageTranslated = null;
		try {
			ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
			messageSource.setBasenames("messages/messages", "anubis/messages");
			messageSource.setDefaultEncoding("UTF-8");
			messageSource.setCacheSeconds(1);
			Locale locale = LocaleContextHolder.getLocale();
			messageTranslated = messageSource.getMessage(message, arg, locale);
		} catch (NoSuchMessageException e) {
			messageTranslated = message; //; + "*NT";
		}
		return messageTranslated;
	}
	
	@Cacheable
	public static String formatMessage(String error){
		if(error == null) {
			return null;
		}
		if(error.contains("#")){
			String[] args = error.split("#");
			String message = "";
			String[] attrs = new String[args.length-1];
			boolean first = true;
			for(int i=0;i<args.length;i++) {
				if(first){
					message = args[i];
					first = false;
				} else {
					attrs[i-1] = verificarVariavelMessage(args[i]);
				}
			}
			return MessageSystem.getMessage(message, attrs);
		} else {
			return MessageSystem.getMessage(error);
		}
	}
	
	@Cacheable
	public static String formatMessage(String message, String... params){
		if(params == null || params.length == 0) {
			return MessageSystem.getMessage(message);
		}
		String[] paramLocale = new String[params.length];
		for(int i=0; i<params.length; i++){
			paramLocale[i] = MessageSystem.getMessage(params[i]);
		}
		return MessageSystem.getMessage(message, paramLocale);
	}
	
	private static String verificarVariavelMessage(String variavel){
		if(variavel.startsWith("{") && variavel.endsWith("}")){
			variavel = variavel.substring(1, variavel.length()-1);
			return MessageSystem.getMessage(variavel);
		} else {
			return variavel;
		}
	}
	
}
