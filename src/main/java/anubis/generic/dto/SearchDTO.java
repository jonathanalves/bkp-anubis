package anubis.generic.dto;

import javax.validation.constraints.NotNull;

import anubis.annotation.NotNullAndNotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDTO {

	@NotNullAndNotEmpty(message = "crud.validation.required#field")
	private String field;
	
	@NotNull(message = "crud.validation.required#countRecord")
	private Integer countRecord;
	
	@NotNullAndNotEmpty(message = "crud.validation.required#query")
	private String query;

	
	public SearchDTO() {
		super();
	}
	
}
