package Login;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Admin.Admin;
import InterfacciaMagazziniere.FrameMagazziniere;
import InterfacciaResponsabileNegozio.FrameResponsabileNegozio;
import InterfacciaSegreteriaCatenaNegozi.FrameSegreteriaCatenaNegozi;
import MyList.MyList;
import MyList.MyListProxyFactory;
import Utenti.Magazziniere;
import Utenti.ResponsabileNegozio;
import Utenti.SegreteriaCatenaNegozi;
import Utenti.Utente;

public class LoginPageListener implements ActionListener, KeyListener{
	private LoginPage frame;

	public LoginPageListener(LoginPage frame) { 
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e){
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Conferma")) {
			confirmButtonPressed();
		}
		else {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
	}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '\n')
			confirmButtonPressed();
	}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}
	
	private void confirmButtonPressed() {
		String nome = frame.getCampoNome().getText();
		String pass = frame.getCampoPass().getText();
		
		if(nome.length() == 0 && pass.length() == 0) {
			setErrorZone("Nessuna credenziale inserita");
		}
		else if(nome.length() == 0 || pass.length() == 0){
			setErrorZone("Completare tutti i campi");
		}
		else if(nome.equals("admin") && pass.equals("admin")) {
			new Admin();
			setErrorZone("");
			frame.getCampoNome().setText("");
			frame.getCampoPass().setText("");
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		else {
			MyList proxyList = new MyListProxyFactory("UserDataBase");
			Utente myUser = null;
			
			for(Object u: proxyList.getArrayList()) {
				if(u instanceof Utente && 
						nome.equals(((Utente) u).getUsername()) && 
						pass.equals(((Utente) u).getPassword())) {
					myUser = (Utente) u;
					break;
				}
			}
			
			if(myUser != null) {
				if( myUser instanceof ResponsabileNegozio) {
					new FrameResponsabileNegozio(myUser);
				}
				else if( myUser instanceof Magazziniere) {
					new FrameMagazziniere();
				}
				else if( myUser instanceof SegreteriaCatenaNegozi) {
					new FrameSegreteriaCatenaNegozi();
				}
				
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				setErrorZone("");
				frame.getCampoNome().setText("");
				frame.getCampoPass().setText("");
				frame.setVisible(false);
			}
			else {
				setErrorZone("Non sei registrato nel sistema");
				frame.getCampoNome().setText("");
				frame.getCampoPass().setText("");
			}
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
}
