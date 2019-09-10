package DataBase;
import java.io.*;

import MainClass.Negozio;

public class NegoziDataBase implements DataBase {

	
	public Negozio[] readData() {
		Negozio[] negoziArray = new Negozio[0];
		
		try { 	
			File inFile = new File(path + "NegoziDataBase");
			if(inFile.length() != 0){
				FileInputStream inputStream = new FileInputStream(inFile);
				ObjectInputStream reader = new ObjectInputStream(inputStream);
				
				negoziArray = (Negozio[]) reader.readObject();
				
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

		return negoziArray;
	}

	
	public void writeData(Object[] objectArray) {
		if(objectArray instanceof Negozio[]) {
			try {
				File file = new File(path + "NegoziDataBase");
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
