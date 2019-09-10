package MainClass;
import java.util.*;
import java.io.Serializable;

public class Articolo implements Serializable{
	private String nome;
	private String descrizione;
	private String sport;	
	private AbstractList<String> materiali = new ArrayList<String>();
	private String code;
	private String prezzo;
	private Calendar cal;
	
	public Articolo(String nome, String descrizione, String sport,
			String code,String prezzo, Calendar cal, String...materiali){
		this.nome = nome;
		this.sport = sport;
		this.descrizione = descrizione;
		this.code = code;
		this.prezzo = prezzo;
		this.cal = cal;
		for(String m: materiali)
			this.materiali.add(m);
	}
	
	public Articolo(String nome, String descrizione, String sport, AbstractList<String> materiali,
			String code,String prezzo, Calendar cal){
		this.nome = nome;
		this.sport = sport;
		this.descrizione = descrizione;
		this.code = code;
		this.prezzo = prezzo;
		this.cal = cal;
		this.materiali.addAll(materiali);
		
	}
	
	public String toString() {
		return nome + " (" + code + ") - " + prezzo;
		
	}
	
	public Calendar getCalendar() {
		return cal;
	}
	
	public String getPrezzo(){
		return prezzo;
	}
	
	public String getSport(){
		return sport;
	}
	
	public String getDescrizione(){
		return descrizione;
	}
	
	public String getNome(){
		return nome;
	}
	
	public String getCode(){
		return code;
	}
	
	public AbstractList<String> getMateriali(){
	    return materiali;
	}
	
	public String[] getArrayString() {
		String[] arrayString = new String[7];
		arrayString[0] = nome;
		arrayString[1] = descrizione;
		arrayString[2] = sport;
		
		arrayString[3] = "";
		for(String m: materiali) {
			arrayString[3] += m + ',';
		}
		arrayString[3] = arrayString[3].substring(0, arrayString[3].length() - 1);
		
		arrayString[4] = code;
		arrayString[5] = prezzo;
		arrayString[6] = String.valueOf((getCalendar()).get(Calendar.DAY_OF_MONTH)) + "/"
						+ (String.valueOf((getCalendar()).get(Calendar.MONTH) + 1)) + "/"
						+ String.valueOf((getCalendar()).get(Calendar.YEAR));
		
		return arrayString;
	}
	
	public int hashCode() {
		int sum = 0;
		for(String m: materiali) {
			sum += m.hashCode();
		}
		return nome.hashCode() ^ descrizione.hashCode() ^ sport.hashCode() ^
				sum ^ code.hashCode() ^ prezzo.hashCode() ^
				(String.valueOf((getCalendar()).get(Calendar.MONTH))).hashCode()
				^ (String.valueOf((getCalendar()).get(Calendar.YEAR))).hashCode()
				^ (String.valueOf((getCalendar()).get(Calendar.DAY_OF_MONTH))).hashCode();
	}
	
}
