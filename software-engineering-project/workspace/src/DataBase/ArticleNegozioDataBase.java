package DataBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import MainClass.ArticoloCount;

public class ArticleNegozioDataBase implements DataBase {
	private String filename;
	
	public ArticleNegozioDataBase(String filename) {
		this.filename = filename;
	}
	
	public ArticoloCount[] readData() {
		ArticoloCount[] articoliArray = new ArticoloCount[0];
		
		try {
			File inFile = new File(path + filename);
			if(inFile.length() != 0){
				FileInputStream inputStream = new FileInputStream(inFile);
				ObjectInputStream reader = new ObjectInputStream(inputStream);
				
				articoliArray = (ArticoloCount[]) reader.readObject();
				
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
		if(objectArray instanceof ArticoloCount[]) {
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
	
	public static void createDataFolder(String utente) throws IOException {
		new File(path + utente).mkdir();
		new File(path + utente + "/ArticleNegozioDataBase").createNewFile();
		new File(path + utente + "/OrderNegozioDataBase").createNewFile();
	}
	
	public static void removeDataFolder(String utente) {
		new File(path + utente + "/ArticleNegozioDataBase").delete();
		new File(path + utente + "/OrderNegozioDataBase").delete();
		new File(path + utente).delete();
	}

}
