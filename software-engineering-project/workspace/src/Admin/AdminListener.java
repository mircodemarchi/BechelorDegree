package Admin;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import DataBase.ArticleNegozioDataBase;
import Login.LoginPage;
import MainClass.Negozio;
import MyList.MyList;
import MyList.MyListProxyFactory;
import Utenti.Magazziniere;
import Utenti.ResponsabileNegozio;
import Utenti.SegreteriaCatenaNegozi;
import Utenti.Utente;

public class AdminListener implements ActionListener, WindowListener, KeyListener {
	private Admin frame;
	
	public AdminListener(Admin frame) { 
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Conferma")) {
			confirmButtonPressed();
		}
		else if(button.getText().equals("Torna a LoginPage")){
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		else if(button.getText().equals("Rimuovi")){
			if(frame.getTable().getSelectedRow() == -1) {
				setErrorZone("Seleziona un utente");
			}
			else {
				if(LoginPage.getProxyListUtenti().getArrayList().get(frame.getTable().getSelectedRow()) instanceof ResponsabileNegozio) {
					for(Object n: LoginPage.getProxyListNegozi().getArrayList()) {
						if(n instanceof Negozio && ((Negozio) n).getUser().equals(LoginPage.getProxyListUtenti().getArrayList().get(frame.getTable().getSelectedRow()))) {
							LoginPage.getProxyListNegozi().removeObject(n);
							ArticleNegozioDataBase.removeDataFolder(((Negozio) n).getUser().getUsername());
							break;
						}
					}
				}
				
				LoginPage.getProxyListUtenti().removeObject(LoginPage.getProxyListUtenti().getArrayList().get(frame.getTable().getSelectedRow()));
				frame.updateTable();
				frame.setVisible(true);
			}
		}

	}
	
	private void confirmButtonPressed() {
		String nome = frame.getCampoNome().getText();
		String pass = frame.getCampoPass().getText();
		String utente = (String) frame.getCampoUtente().getSelectedItem();
		
		if(nome.length() == 0 && pass.length() == 0) {
			setErrorZone("Nessuna credenziale inserita!!!");
		}
		else if(nome.length() == 0 || pass.length() == 0){
			setErrorZone("Completare tutti i campi!!!");
		}
		else if (controlCheck(nome)) {
			frame.setVisible(true);
		}
		else {
			Utente user;
			
			if(utente.equals("Magazziniere")) {
				user = new Magazziniere(nome, pass);
			}
			else if(utente.equals("SegreteriaCatenaNegozi")) {
				user = new SegreteriaCatenaNegozi(nome, pass);
			}
			else {
				user = null;
			}
			
			LoginPage.getProxyListUtenti().addObject(user);
			setErrorZone("Operazione completata");
			
			frame.getCampoNome().setText("");
			frame.getCampoPass().setText("");
			frame.updateTable();
			frame.setVisible(true);
		}
	}
	
	private void setErrorZone(String err) {
		frame.getErrorZone().setVisible(false);
		frame.getErrorZone().removeAll();
		JLabel error = new JLabel(err);
		error.setForeground(Color.RED);
		frame.getErrorZone().add(error);
		frame.getErrorZone().setVisible(true);
	}
	
	private boolean controlCheck (String utente) {

		for (Object a : LoginPage.getProxyListUtenti().getArrayList()) {
			if (((Utente) a).getUsername().equalsIgnoreCase(utente)) {
				setErrorZone("Utente già presente");
				frame.getCampoNome().setText("");
				frame.getCampoPass().setText("");
				return true;
			}
		}
		return false;
	
	}

	public void windowOpened(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		LoginPage.getProxyListUtenti().saveData();
		LoginPage.getProxyListNegozi().saveData();
		LoginPage.getLoginPage().setVisible(true);
	}

	public void windowClosed(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}

	public void keyTyped(KeyEvent e) {
			
			if(e.getSource() instanceof JTextField) {

				if(e.getKeyChar() == '\n')
					confirmButtonPressed();
			}
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
