package anubis.validator;

import anubis.generic.dto.QueryDataTablesDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("sqlInjectionValidator")
public class SqlInjectionValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return QueryDataTablesDTO.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		QueryDataTablesDTO dto = (QueryDataTablesDTO) arg0;
		String where = dto.getWhere().toLowerCase();
		if (where.contains("array_upper") 	|| 
			where.contains("xpath") 		|| 
			where.contains("query_to_xml") 	|| 
			where.contains("or") 			|| 
			where.contains("select") 		|| 
			where.contains("delete") 		|| 
			where.contains("update")		||
			where.contains(";")) {
			arg1.reject("HQL injection detected.");
		}
	}

}
