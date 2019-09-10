package MainClass;
import java.io.Serializable;
import java.util.*;

import Login.LoginPage;

public class Ordine implements Serializable{
	private Negozio negozio;
	private String codiceOrdine;
	private Calendar data;
	private HashMap<Articolo, Integer> quantitàPerArticolo = new HashMap<Articolo, Integer>();
	
	public Ordine(Negozio negozio, String codiceOrdine, Calendar calendar, HashMap<Articolo, Integer> quantitàPerArticolo) {
		this.negozio = negozio;
		this.codiceOrdine = codiceOrdine;
		this.data = calendar;
		this.quantitàPerArticolo = quantitàPerArticolo;
	}
	
	public Ordine(Negozio negozio, String codiceOrdine, GregorianCalendar data) {
		this(negozio, codiceOrdine, data, new HashMap<Articolo, Integer>());
	}
	
	public Ordine(Negozio negozio, String codiceOrdine) {
		this(negozio, codiceOrdine, new GregorianCalendar());
	}
	
	public Ordine(Negozio negozio) {
		this.negozio = negozio;
		
		Random ran = new Random();
		int n;
		do {
			n = ran.nextInt(10000);
			codiceOrdine = Integer.toString(n);
		}while(checkRandom(n));
		data = new GregorianCalendar();
	}
	
	private boolean checkRandom(int n) {
		for(Object o: LoginPage.getProxyListOrdini().getArrayList()) {
			if(o instanceof Ordine && ((Ordine) o).getCodiceOrdine().equals(Integer.toString(n))) {
				return true;
			}
		}
		
		return false;
	}

	public Ordine(Ordine ordine) {
		this(ordine.getNegozio(), ordine.getCodiceOrdine(), ordine.getData(), new HashMap<Articolo, Integer>(ordine.getQuantitàPerArticolo()));
	}

	public void put(Articolo articolo, int quantità) throws NumberFormatException{
		quantitàPerArticolo.put(articolo, quantità);
	}
	
	public void remove(Articolo articolo){
		quantitàPerArticolo.remove(articolo);
	}
	
	public Negozio getNegozio() {
		return negozio;
	}
	
	public String getCodiceOrdine() {
		return codiceOrdine;
	}
	
	public Calendar getData() {
		return data;
	}
	
	public HashMap<Articolo, Integer> getQuantitàPerArticolo(){
		return quantitàPerArticolo;
	}
	
	public String getPrezzo() {
		double prezzo = 0.0;
		for(Articolo a: quantitàPerArticolo.keySet()) {
			prezzo += Double.parseDouble(a.getPrezzo()) * quantitàPerArticolo.get(a);
		}

		return String.format("%.2f", prezzo);
	}
	
	public String[] getArrayString() {
		String[] arrayString = new String[4];
		arrayString[0] = codiceOrdine;
		arrayString[1] = "" + data.get(Calendar.DAY_OF_MONTH) + '/' + (int)(data.get(Calendar.MONTH) + 1) + '/' + data.get(Calendar.YEAR);
		
		int numeroArticoli = 0;
		for(Articolo a: quantitàPerArticolo.keySet()) {
			numeroArticoli += quantitàPerArticolo.get(a);
		}
		arrayString[2] = Integer.toString(numeroArticoli);
		
		arrayString[3] = getPrezzo() + "€";
		
		return arrayString;
	}

	public Object[] getLongArrayString() {
		Object[] arrayString = new Object[7];
		arrayString[0] = codiceOrdine;
		arrayString[1] = "" + data.get(Calendar.DAY_OF_MONTH) + '/' + (int)(data.get(Calendar.MONTH) + 1) + '/' + data.get(Calendar.YEAR);
		
		int numeroArticoli = 0;
		for(Articolo a: quantitàPerArticolo.keySet()) {
			numeroArticoli += quantitàPerArticolo.get(a);
		}
		arrayString[2] = numeroArticoli;
		
		arrayString[3] = getPrezzo() + "€";
		
		arrayString[4] = negozio.getNomeCatena();
		
		arrayString[5] = negozio.getUser().getUsername();
		
		arrayString[6] = negozio.getIndirizzo();
		
		return arrayString;
	}
	
}
