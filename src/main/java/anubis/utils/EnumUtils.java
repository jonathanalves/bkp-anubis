package anubis.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import anubis.MessageSystem;

@Component("enumUtils")
public class EnumUtils {

	//FIXME validar e traduzir os enums no sistema
	@Cacheable
	public static <T extends Enum<T>> Object listEnum(Class<T> clazz){
		return EnumSet.allOf(clazz).stream().map((obj) -> {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", obj.name());
			map.put("nome", translateEnum(obj));
			return map;
		}).collect(Collectors.toList());
	}

	//FIXME validar e traduzir os enums no sistema
	@Cacheable
	public static <T extends Enum<T>> String translateEnum(T obj){
		return MessageSystem.formatMessage("enum." + obj.getClass().getSimpleName().toLowerCase() + "." + obj.name().toLowerCase().replaceAll("_",  "."));
	}
	
}
