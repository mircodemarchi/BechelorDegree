package MainClass;

import java.io.Serializable;
import java.util.Calendar;

public class ArticoloCount implements Serializable{
	private Articolo articolo;
	private int count;
	
	public ArticoloCount(Articolo articolo, int count) {
		this.articolo = articolo;
		this.count = count;
	}
	
	public Articolo getArticolo() {
		return articolo;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public Object[] getArrayString() {
		Object[] arrayObject = new Object[8];
		arrayObject[0] = articolo.getNome();
		arrayObject[1] = articolo.getDescrizione();
		arrayObject[2] = Integer.toString(count);
		arrayObject[3] = articolo.getSport();
		arrayObject[4] = articolo.getMateriali();
		arrayObject[5] = articolo.getCode();
		arrayObject[6] = articolo.getPrezzo();
		arrayObject[7] = String.valueOf((articolo.getCalendar()).get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf((articolo.getCalendar()).get(Calendar.MONTH)) + "/"
				+ String.valueOf((articolo.getCalendar()).get(Calendar.YEAR));
		
		return arrayObject;
	}
	
	public boolean equals(Object other) {
		return other instanceof ArticoloCount && this.getArticolo().getCode().equals(((ArticoloCount) other).getArticolo().getCode());
	}
}
