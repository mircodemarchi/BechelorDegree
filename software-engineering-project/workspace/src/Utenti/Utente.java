package Utenti;
import java.io.Serializable;

public abstract class Utente implements Serializable{
	private String username;
	private String password;
	
	public Utente(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public boolean equals(Object other) {
		return other instanceof Utente && 
				this.username.equals(((Utente) other).username) &&
				this.password.equals(((Utente) other).password);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public Object[] getArrayString() {
		Object[] arrayString = new Object[3];
		
		arrayString[0] = username;
		
		if(this instanceof Magazziniere) {
			arrayString[1] = "Magazziniere";
		}
		else if(this instanceof ResponsabileNegozio) {
			arrayString[1] = "ResponsabileNegozio";
		}
		else if(this instanceof SegreteriaCatenaNegozi) {
			arrayString[1] = "SegreteriaCatenaNegozi";
		}
		else {
			arrayString[1] = "null";
		}
		
		arrayString[2] = password;
		
		return arrayString;
	}
}
