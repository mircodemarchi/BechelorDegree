package MainClass;

import java.io.*;
import Utenti.ResponsabileNegozio;

public class Negozio implements Serializable {
	private String code;
	private String name;
	private ResponsabileNegozio user;
	private String indirizzo;
	private String city;
	
	public Negozio(String code, String name, String indirizzo, ResponsabileNegozio user, String city) {
		this.code = code;
		this.name = name;
		this.user = user;
		this.indirizzo = indirizzo;
		this.city = city;
	}
	
	public String toString() {
		return code + ';' + name + ';' + 
				indirizzo + ';' + user.getUsername() + ';'+ city + ';';
	}
	
	public String getCode() {
		return code;
	}
	
	public ResponsabileNegozio getUser() {
		return user;
	}
	
	public String getNomeCatena() {
		return name;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public String getCity() {
		return city;
	}
	
	public String[] getArrayString() {
		String[] arrayString = new String[5];
		arrayString[0] = code;
		arrayString[1] = name;
		arrayString[2] = indirizzo;
		arrayString[3] = city;
		arrayString[4] = user.getUsername();
		return arrayString;
	}
	
	public boolean equals(Object other) {
		return other instanceof Negozio && this.getCode().equals(((Negozio) other).getCode());
	}
	
	@Override
	public int hashCode() {
		
		return name.hashCode() ^ indirizzo.hashCode() ^ city.hashCode() ^
				code.hashCode() ^ (user.getUsername()).hashCode();
	}
	
}
