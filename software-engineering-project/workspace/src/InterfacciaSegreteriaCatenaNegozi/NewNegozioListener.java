package InterfacciaSegreteriaCatenaNegozi;
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import DataBase.ArticleNegozioDataBase;
import MainClass.Negozio;
import MyList.MyListProxyFactory;
import Utenti.ResponsabileNegozio;
import Utenti.Utente;

public class NewNegozioListener implements ActionListener, KeyListener {
	private PanelNewNegozio panel;
	private FrameSegreteriaCatenaNegozi frame;
	
	public NewNegozioListener(PanelNewNegozio panel,
			FrameSegreteriaCatenaNegozi frame) {
		this.panel = panel;
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Aggiungi Negozio")) {
			confirmButtonAddPressed();
		}
		else if(button.getText().equals("Rimuovi Negozio")) {
			
			try {
				ArticleNegozioDataBase.removeDataFolder(((Negozio) frame.getProxyListNegozi().getArrayList().get(panel.getTableNegozi().getSelectedRow())).getUser().getUsername());
				
				for(Object u: frame.getProxyListUsers().getArrayList()) {
					if(u instanceof Utente && ((Utente) u).equals(((Negozio) frame.getProxyListNegozi().getArrayList().get(panel.getTableNegozi().getSelectedRow())).getUser())) {
						frame.getProxyListUsers().removeObject((Utente) u);
						break;
					}
				}
				
				frame.getProxyListNegozi().removeObject(frame.getProxyListNegozi().getArrayList().get(panel.getTableNegozi().getSelectedRow()));
				frame.getProxyListNegozi().displayNegozi(panel.getTableModelNegozi(), panel.getTableNegozi(), panel.getCentroCentro());
				frame.setVisible(true);
			}
			catch (IndexOutOfBoundsException e1) {
				setErrorZone("Nessuna cella selezionata");
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		
		if(e.getSource() instanceof JTextField) {

			if(e.getKeyChar() == '\n')
				confirmButtonAddPressed();
		}
	}

	private void confirmButtonAddPressed () {
		
		String nome = panel.getCampoNome().getText();
		String indirizzo = panel.getCampoIndirizzo().getText();
		String city = panel.getCampoCity().getText();
		
		String utente = panel.getCampoUtente().getText();
		String pass = panel.getCampoPassword().getText();
		boolean anotherone = true;
		int cod = 0;
		while (anotherone) {

			anotherone = false;
			cod = ThreadLocalRandom.current().nextInt(1, 998 + 1);
			for (Object a : frame.getProxyListNegozi().getArrayList()) {
				if (((Negozio) a).getCode().equalsIgnoreCase(((Integer) cod).toString())) {
					anotherone = true;
				}
			}
			
		}
		
		if(nome.length() == 0 && indirizzo.length() == 0 && city.length() == 0 
				&& utente.length() == 0 && pass.length() == 0) {
			setErrorZone("Nessuna credenziale inserita");
		}
		
		else if(nome.length() == 0 || indirizzo.length() == 0 || city.length() == 0 
				&& utente.length() == 0 && pass.length() == 0){
			setErrorZone("Completare tutti i campi");
		}
		else if (controlCheck(utente)) {
			frame.setVisible(true);
		}
		else {
			
			frame.getProxyListNegozi().addObject(new Negozio(((Integer) cod).toString(), nome, indirizzo, new ResponsabileNegozio(utente, pass), city));
			frame.getProxyListUsers().addObject(new ResponsabileNegozio(utente, pass));
			
			try {
				ArticleNegozioDataBase.createDataFolder(utente);
			} catch(IOException e) {
				System.out.println("Errore creazione file!");
			}
			
			panel.getCampoNome().setText("");
			panel.getCampoIndirizzo().setText("");
			panel.getCampoCity().setText("");
			panel.getCampoUtente().setText("");
			panel.getCampoPassword().setText("");
		
			setErrorZone("Operazione completata");
			
			frame.getProxyListNegozi().displayNegozi(panel.getTableModelNegozi(), panel.getTableNegozi(), panel.getCentroCentro());
			frame.setVisible(true);
		}
	}
	
	private boolean controlCheck (String utente) {

		for (Object a : frame.getProxyListUsers().getArrayList()) {
			if (((Utente) a).getUsername().equalsIgnoreCase(utente)) {
				setErrorZone("Utente già presente");
				panel.getCampoUtente().setText("");
				return true;
			}
		}
		return false;
	
	}
	
	private void setErrorZone(String err) {
		panel.getErrorZone().setVisible(false);
		panel.getErrorZone().removeAll();
		JLabel error = new JLabel(err);
		error.setForeground(Color.RED);
		panel.getErrorZone().add(error);
		panel.getErrorZone().setVisible(true);
	}

}
