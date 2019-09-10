package DataBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import Utenti.Utente;

public class UserDataBase implements DataBase {

	@Override
	public Utente[] readData() {
		Utente[] utentiArray = new Utente[0];
		
		try { 	
			File inFile = new File(path + "UserDataBase");
			if(inFile.length() != 0){
				FileInputStream inputStream = new FileInputStream(inFile);
				ObjectInputStream reader = new ObjectInputStream(inputStream);
				
				utentiArray = (Utente[]) reader.readObject();
				
				reader.close();
				inputStream.close();
			}
		}
		catch (ClassNotFoundException e1) {
			System.out.println("Classe non trovata!");
		}
		catch (IOException e2) {
			System.out.println("Si è verificata un'eccezione di I/O 1");
		}

		return utentiArray;
	}

	@Override
	public void writeData(Object[] objectArray) {
		if(objectArray instanceof Utente[]) {
			try {
				File file = new File(path + "UserDataBase");
				OutputStream outStream = new FileOutputStream(file);
				ObjectOutputStream writer = new ObjectOutputStream(outStream);
				
				writer.writeObject(objectArray);
				
				writer.close();
				outStream.close();
				
			}
			catch (IOException e) {
				System.out.println("Si è verificata un'eccezione di I/O 2");
			}	
		}
		
	}

}
