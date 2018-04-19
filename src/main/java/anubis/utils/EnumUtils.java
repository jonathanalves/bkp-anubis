package anubis.utils;

import anubis.MessageSystem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component("enumUtils")
public class EnumUtils {

	//FIXME validar e traduzir os enums no sistema
	@Cacheable
	public static <T extends Enum<T>> Object listEnum(Class<T> clazz){
		return EnumSet.allOf(clazz).stream().map((obj) -> {
			HashMap<String, String> map = new HashMap<>();
            map.put("id", obj.name());
            map.put("nome", translateEnum(obj));
            System.out.println(translateEnum(obj));
			return map;
		}).collect(Collectors.toList());
	}

	//FIXME validar e traduzir os enums no sistema
	@Cacheable
	public static <T extends Enum<T>> String translateEnum(T obj){
		return MessageSystem.formatMessage("enum." + getClassName(obj.getClass()).toLowerCase().replaceAll("enum", "") + "." + obj.name().toLowerCase().replaceAll("_",  "."));
	}

	public static String getClassName(Class c) {
		String nomeClasse = c.getName();
		int lastChar;
		lastChar = nomeClasse.lastIndexOf ('.') + 1;
		if ( lastChar > 0 ) {
			nomeClasse = nomeClasse.substring ( lastChar );
		}
		nomeClasse = nomeClasse.replaceAll("\\$([0-9]*)", "");
		return nomeClasse;
	}
	
}
