package anubis.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

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

}
