package anubis.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("collectionUtils")
public class CollectionUtils {

	public static <T> List<T> convertList(List<Map<String, Object>> itens, Class<T> clazz){
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(Map<String, Object> obj : itens) {
            Map<String, Object> map =  mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
            list.add(mapper.convertValue(map, clazz));
        }
		return list;
	}
	
	public static boolean compareWithMany(Object first, Object next, Object... rest) {
		if (first.equals(next))
			return true;
		for (int i = 0; i < rest.length; i++) {
			if (first.equals(rest[i]))
				return true;
		}
		return false;
	}
	
}
