package anubis.generic.dto;

import java.io.Serializable;
import java.util.Calendar;

import anubis.response.ResponseException;
import anubis.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodoDTO implements Serializable {

	private static final long serialVersionUID = 5210413339257337716L;
	
	private Calendar inicio;
	private Calendar fim;
	
	public PeriodoDTO() {
		super();
	}

	public PeriodoDTO(String inicio, String fim) {
		super();
		this.gerar(DateUtils.getDiaMesAno(inicio), DateUtils.getDiaMesAno(fim));
	}

	public PeriodoDTO(Calendar inicio, Calendar fim) {
		super();
		this.gerar(inicio, fim);
	}

	public PeriodoDTO(Calendar mes) {
		super();
		Calendar data = (Calendar) mes.clone();
		data.set(Calendar.DAY_OF_MONTH, 1);
		this.gerar(data, DateUtils.getDataNoUltimoDia(data.get(Calendar.YEAR), data.get(Calendar.MONTH) + 1));
	}
	
	protected void gerar(Calendar inicio, Calendar fim) {
		
		if(inicio == null || fim == null){
			throw new ResponseException("business.periodo.invalido");
		}
		
		this.inicio = (Calendar) inicio.clone();
		this.fim = (Calendar) fim.clone();
		
		/* zera horario de inicio */
		this.inicio.set(Calendar.HOUR_OF_DAY, 0);
		this.inicio.set(Calendar.MINUTE, 0);
		this.inicio.set(Calendar.SECOND, 0);
		this.inicio.set(Calendar.MILLISECOND, 0);

		/* zera horario de fim */
		this.fim.set(Calendar.HOUR_OF_DAY, 23);
		this.fim.set(Calendar.MINUTE, 59);
		this.fim.set(Calendar.SECOND, 59);
		this.fim.set(Calendar.MILLISECOND, 999);
		
		if( this.inicio.after(this.fim) ){
			throw new ResponseException("business.periodo.invalido.inicio.maior.fim");
		}
	}
	
    public String toString() { 
        return DateUtils.getDiaMesAno(this.inicio) + " - " + DateUtils.getDiaMesAno(this.fim);
    } 
	
    public boolean isMonth(){
    	
    	PeriodoDTO mes = new PeriodoDTO(this.getInicio());
    	
    	if(mes.getInicio().equals(this.getInicio()) && mes.getFim().equals(this.getFim())){
    		return true;
    	}
    	
    	return false;
    }
    
    public void validarPeriodo(int dias){
    	if(DateUtils.getDiferencaEmDias(this.inicio, this.fim, true) > dias){
    		//FIXME PHILIPE INTERNACIONALIZAR
    		throw new ResponseException("Período muito longo");
    	}
    }
    
}
