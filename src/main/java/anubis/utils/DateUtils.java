package anubis.utils;

import anubis.generic.dto.PeriodoDTO;
import anubis.response.ResponseException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component("dateUtils")
public class DateUtils {

	public static String getHoraMinuto(Calendar time) {
		if (time != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			return sdf.format(time.getTime());
		}
		return null;
	}
	
	public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

	public static Calendar getHoraMinuto(String time) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				calendar.setTime(sdf.parse(time));
				return calendar;
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.hora");
		}
	}

	public static String getHoraMinutoSegundo(Calendar time) {
		if (time != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			return sdf.format(time.getTime());
		}
		return null;
	}

	public static Boolean isHoje(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date.getTime()).equals(sdf.format(DateUtils.getCalendar().getTime()));
        }
		return false;
	}
	
	public static String getDiaMesAno(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static Integer getDiaMesAnoRelatorio(String dateString) {
		Calendar date = getDiaMesAno(dateString);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(sdf.format(date.getTime()));
	}

	public static String getDiaMesAnoPortugues(String dateString) {
		return getDiaMesAnoPortugues(getDiaMesAno(dateString));
	}

	public static String getDiaMesAnoPortugues(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static String getMesAnoPortugues(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static String getMesAno(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static Integer getMesAnoRelatorio(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
			return Integer.parseInt( sdf.format(date.getTime()) );
		}
		return null;
	}

	public static Calendar getDiaMesAno(String time) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(sdf.parse(time));
				return zeraHorario(calendar);
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.data");
		}
	}

	public static String getDiaMesAnoHoraMinutoSegundo(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date.getTime());
		}
		return null;
	}
	
	public static String getDiaMesAnoTHoraMinutoSegundo(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static String getDiaMesAnoHoraMinutoSegundoMs(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static String getDiaMesAnoHoraMinutoSegundoArquivo(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static Calendar getDiaMesAnoHoraMinutoSegundoPortugues(String time) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				calendar.setTime(sdf.parse(time));
				return calendar;
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.data");
		}
	}
	
	public static Calendar getDiaMesAnoHoraMinutoSegundo(String time) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				calendar.setTime(sdf.parse(time));
				return calendar;
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.data");
		}
	}

	public static String getDiaMesAnoHoraMinutoSegundoBackupFormat(Calendar date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
			return sdf.format(date.getTime());
		}
		return null;
	}

	public static Calendar getDiaMesAnoHoraMinutoSegundoBackupFormat(String time) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
				calendar.setTime(sdf.parse(time));
				return calendar;
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.data");
		}
	}
	
	
	public static Calendar getHoraMinutoSegundo(String time) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				calendar.setTime(sdf.parse(time));
				return calendar;
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.data");
		}
	}

	public static String getDiferencaDateHoraMinutoSegundo(Calendar dataInicio, Calendar dataFim) {
		long difMilli = dataFim.getTimeInMillis() - dataInicio.getTimeInMillis();

		int timeInSeconds = (int) difMilli / 1000;
		
		if(timeInSeconds < 0){
			timeInSeconds = timeInSeconds * -1;
		}

		int hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		int minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		int seconds = timeInSeconds;
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}
	
	public static Integer getDiferencaDateHora(Calendar dataInicio, Calendar dataFim) {
		long difMilli = dataFim.getTimeInMillis() - dataInicio.getTimeInMillis();

		int timeInSeconds = (int) difMilli / 1000;
		
		if(timeInSeconds < 0){
			timeInSeconds = timeInSeconds * -1;
		}

		return  timeInSeconds / 3600;
	}
	
	public static String getSecondsToHour(Integer timeInSeconds){
		
		if(timeInSeconds == null || timeInSeconds.equals(0)){
			return "00:00:00";
		}
		
		if(timeInSeconds < 0){
			timeInSeconds = timeInSeconds * -1;
		}
		
		int hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		int minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		int seconds = timeInSeconds;
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		
	}
	
	public static Integer getTimeInSeconds(Calendar calendar) {
		int hora = calendar.get(Calendar.HOUR_OF_DAY) * 3600;
		int minuto = calendar.get(Calendar.MINUTE) * 60;
		int segundo = calendar.get(Calendar.SECOND);
		return hora + minuto + segundo;
	}

	public static Integer getTimeInMinutes(Calendar calendar) {
		int hora = calendar.get(Calendar.HOUR_OF_DAY) * 60;
		int minuto = calendar.get(Calendar.MINUTE);
		return hora + minuto;
	}

	public static Integer getTimeInMinutes(String time) {
		return getTimeInMinutes(getHoraMinutoSegundo(time));
	}

	public static Integer getTimeInSeconds(String time) {
		return getTimeInSeconds(getHoraMinutoSegundo(time));
	}

	public static Integer diasMes(Integer ano, Integer mes) {
		Calendar calendar = DateUtils.getCalendar();
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static Calendar getDataNoUltimoDia(Integer ano, Integer mes) {
		Calendar calendar = getCalendarObject(ano, mes);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar;
	}

	public static Calendar getCalendarObject(Integer ano, Integer mes) {

		if(!DateUtils.validarAno(ano)) {
			throw new ResponseException("utils.date.anoinvalido");
		}
		if(!DateUtils.validarMes(mes)) {
			throw new ResponseException("utils.date.mesinvalido");
		}
		
		Calendar calendar = DateUtils.getCalendar();
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return zeraHorario(calendar);
	}

	public static Integer finaisSemanaMes(Integer ano, Integer mes) {
		
		mes = mes - 1;
		Integer quantidade = 0;
		Calendar calendar = DateUtils.getCalendar();
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.MONTH, mes);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		while (calendar.get(Calendar.MONTH) == mes) {
			if (isFinalDeSemana(calendar)) {
				quantidade++;
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return quantidade;
		
	}

	public static Integer finaisSemanaPeriodo(Calendar inicio, Calendar fim) {
		Integer quantidade = 0;
		while (inicio.before(fim) && inicio.equals(fim)) {
			if (isFinalDeSemana(inicio)) {
				quantidade++;
			}
			inicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		return quantidade;
	}

	public static Calendar getAmanha() {
		Calendar calendar = DateUtils.getCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar;
	}

	public static boolean isFinalDeSemana(Calendar data) {
        return data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

	public static Integer getSecondsFromTime(Calendar data) {
		return (60 * 60 * data.get(Calendar.HOUR_OF_DAY)) + (60 * data.get(Calendar.MINUTE)) + data.get(Calendar.SECOND);
	}
	
	public static Double secondsToHour(Integer segundos) {
		if(segundos > 0){
			return segundos * 1d / 60 / 60;
		}
		return 0d;
	}
	
	public static boolean validarAno(Integer ano) {
		return ano > 2000;
	}

	public static boolean validarMes(Integer mes) {
		return mes >= 1 && mes <= 12;
	}

	public static void validarMesAno(Integer mes, Integer ano) throws ResponseException {

		if (!validarAno(ano)) {
			throw new ResponseException("utils.date.anoinvalido");
		}
		
		if (!validarMes(mes)) {
			throw new ResponseException("utils.date.mesinvalido");
		}
		
	}

	public static PeriodoDTO getPeriodoCorrente() {
		Calendar date = DateUtils.getCalendar();
		date.set(Calendar.DAY_OF_MONTH, 1);
		return new PeriodoDTO(date, getDataNoUltimoDia(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1));
	}

	public static PeriodoDTO getPeriodo(Calendar date) {
		date.set(Calendar.DAY_OF_MONTH, 1); 
		return new PeriodoDTO(date, getDataNoUltimoDia(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1));
	}

	public static Integer getDiferencaEmDias(Calendar dataInicioBkp, Calendar dataFim, boolean verificarFinalSemana) {

		dataInicioBkp = zeraHorario(dataInicioBkp);
		Calendar dataFimBkp = zeraHorario(dataFim);
		
		Integer days = 0;
		Calendar dataInicio = (Calendar) dataInicioBkp.clone(); 
		while (dataInicio.before(dataFimBkp)) {
			dataInicio.add(Calendar.DAY_OF_MONTH, 1);
			if (!verificarFinalSemana || !isFinalDeSemana(dataInicio)) {
				days++;
			}
		}
		
		return days;
		
	}

	public static Calendar zeraHorario(Calendar time) {
		
		Calendar tempo = (Calendar) time.clone();
		
		tempo.set(Calendar.HOUR_OF_DAY, 0);
		tempo.set(Calendar.MINUTE, 0);
		tempo.set(Calendar.SECOND, 0);
		tempo.set(Calendar.MILLISECOND, 0);
		
		return tempo;
		
	}

	public static String formataDiaMesAno(Integer time) throws ResponseException {
		return formataDiaMesAno(time, "ddMMyyyy");
	}

	public static String formataDiaMesAno(Integer time, String format) throws ResponseException {
		try {
			if (time != null) {
				Calendar calendar = DateUtils.getCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				calendar.setTime(sdf.parse(time.toString()));
				return getDiaMesAnoPortugues(calendar);
			}
			return null;
		} catch (ParseException e) {
			throw new ResponseException(e, "utils.date.formato.incorreto.data");
		}
	}
	
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		DateTime dt = new DateTime();
		calendar.setTimeInMillis(dt.getMillis());
		return calendar;
	}
	
	public static String getMesAtualPortugues() {
		Calendar calendar = Calendar.getInstance();
		switch (calendar.get(Calendar.MONTH)) {
			case 0:	return "Janeiro";
			case 1:	return "Fevereiro";
			case 2:	return "Março";
			case 3: return "Abril";
			case 4: return "Maio";
			case 5: return "Junho";
			case 6: return "Julho";
			case 7: return "Agosto";
			case 8: return "Setembro";
			case 9: return "Outubro";
			case 10: return "Novembro";
			case 11: return "Dezembro";
			default: return "";
		}
	}
	
	public static String getDiaSemanaAtual() {
		Calendar calendar = Calendar.getInstance();
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case 1: return "domingo";
			case 2: return "segunda-feira";
			case 3: return "terça-feira";
			case 4: return "quarta-feira";
			case 5: return "quinta-feira";
			case 6: return "sexta-feira";
			case 7: return "sábado";
			default: return "";
		}
	}
	
}
