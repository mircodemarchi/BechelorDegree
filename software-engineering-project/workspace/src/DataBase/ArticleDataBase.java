package DataBase;
import java.io.*;

import MainClass.Articolo;

public class ArticleDataBase implements DataBase {
	private String filename;
	
	public ArticleDataBase(String filename) {
		this.filename = filename;
	}
	
	public Articolo[] readData() {
		Articolo[] articoliArray = new Articolo[0];
		
		try { 	
			File inFile = new File(path + filename);
			if(inFile.length() != 0){
				FileInputStream inputStream = new FileInputStream(inFile);
				ObjectInputStream reader = new ObjectInputStream(inputStream);
				
				articoliArray = (Articolo[]) reader.readObject();
				
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

		return articoliArray;
	}

	
	public void writeData(Object[] objectArray) {
		if(objectArray instanceof Articolo[]) {
			try {
				File file = new File(path + filename);
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
