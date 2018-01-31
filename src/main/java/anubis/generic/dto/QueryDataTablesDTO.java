package anubis.generic.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class QueryDataTablesDTO implements Serializable {

	private static final long serialVersionUID = -5522660716759268415L;
	
	private Integer quantidade;
	private Integer pagina;
	private Integer draw;
	private String orderBy;
	private String orderByDirection;
	private String search;
	
	private HashMap<String, Object> dados;
	
	public QueryDataTablesDTO() {
		super();
		this.dados = new HashMap<>();
	}
	
	public String getOrderByDirection(){
		return ( this.orderByDirection != null ) ? this.orderByDirection.toLowerCase() : "asc";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getWhere(){

		StringBuilder where = new StringBuilder();

		if(this.getDados().get("where") != null){
			HashMap<String, Object> mp = (HashMap<String, Object>)this.getDados().get("where");
		    Iterator it = mp.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        where.append(" AND ").append(pair.getKey()).append(" = ").append(pair.getValue().toString());
		    }
		    
		}

		if(this.getDados().get("whereIn") != null){
			HashMap<String, Object> mp = (HashMap<String, Object>)this.getDados().get("whereIn");
		    Iterator it = mp.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        where.append(" AND ").append(pair.getKey()).append(" IN ( ").append(pair.getValue().toString()).append(" ) ");
		    }
		}

		if(this.getDados().get("whereCondition") != null){
			List<Object> mp = (List<Object>)this.getDados().get("whereCondition");
		    for (Object dto : mp) {
		        HashMap<String, Object> conditions = (HashMap<String, Object>) dto;
		        where.append(" AND ").append(conditions.get("key").toString()).append(conditions.get("condition").toString()).append(conditions.get("value").toString());
		    }
		}
		
		return where.toString();
		
	}
	
	public Long getNumber(String index){
		if(this.getDados().get(index) != null){
			String valor = this.getDados().get(index).toString();
			return Long.valueOf(valor);
		}
		return null;
	}

	public Boolean getBoolean(String index){
		if(this.getDados().get(index) != null){
			String valor = this.getDados().get(index).toString();
			return Boolean.valueOf(valor);
		}
		return false;
	}

    public List<Long> getIds(String index){
        ArrayList<Integer> list = (ArrayList<Integer>) this.getDados().get(index);
        if(list != null && !list.isEmpty()){
            return list.stream().mapToLong(i -> i).boxed().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
