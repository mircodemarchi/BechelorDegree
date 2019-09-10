package InterfacciaSegreteriaCatenaNegozi;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Login.LoginPage;
import MainClass.Articolo;

public class NewArticoloListener implements ActionListener, KeyListener {
	
	private PanelNewArticolo panel;
	private FrameSegreteriaCatenaNegozi frameSegreteria;

	
	public NewArticoloListener(PanelNewArticolo panel,
			FrameSegreteriaCatenaNegozi frameSegreteria) {
		this.panel = panel;
		this.frameSegreteria = frameSegreteria;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Aggiungi Articolo")) {
			confirmButtonAddPressed();
		}
		else if(button.getText().equals("Rimuovi Articolo")) {
			
			try {
				frameSegreteria.getProxyListArticoli().removeObject(frameSegreteria.getProxyListArticoli().getArrayList().get(panel.getTable().getSelectedRow()));
				frameSegreteria.getProxyListArticoli().displayArticoli(panel.getTableModel(), panel.getTable(), panel.getCentroCentro());
				frameSegreteria.setVisible(true);
			}
			catch (IndexOutOfBoundsException e1) {
				setErrorZone("Nessuna cella selezionata");
			}
		}
		else if(button.getText().equals("+")) {
			confirmButtonPlusPressed();
		}
	}
		
	
	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		
		if(e.getSource() instanceof JTextField) {
			JTextField source = (JTextField) e.getSource();

			if(source.getName() != null && source.getName().equals("campoMateriali") && e.getKeyChar() == '\n')
				confirmButtonPlusPressed();
			
			else if(e.getKeyChar() == '\n')
				confirmButtonAddPressed();
		}
		else if(e.getSource() instanceof JList && e.getExtendedKeyCode() == 8) {
			panel.getArrayListMateriali().remove(panel.getListaMateriali().getSelectedValue());
			String[] arrayStringMateriali = new String[panel.getArrayListMateriali().size()];
			panel.getArrayListMateriali().toArray(arrayStringMateriali);
			panel.getListaMateriali().setListData(arrayStringMateriali);
			panel.getCampoMateriali().setText("");
			frameSegreteria.setVisible(true);

		}
	}

	private void confirmButtonAddPressed () {
		String nome = panel.getCampoNome().getText();
		String descrizione = panel.getCampoDescrizione().getText();
		String sport = panel.getCampoSport().getText();
		AbstractList<String> materiali = new ArrayList<String>(panel.getArrayListMateriali());
		Calendar cal = (panel.getDate()).getCalendar();
		String prezzo = panel.getCampoPrezzo().getText();
		
		boolean anotherone = true;
		int code = 0;
		while (anotherone) {

			anotherone = false;
			code = ThreadLocalRandom.current().nextInt(1, 998 + 1);
			for (Object a : frameSegreteria.getProxyListArticoli().getArrayList()) {
				if (((Articolo) a).getCode().equalsIgnoreCase(((Integer) code).toString())) {
					anotherone = true;
				}
			}
			
		}
		
		if(nome.length() == 0 && descrizione.length() == 0 && sport.length() == 0 && materiali.size() == 0 
				&& prezzo.length() == 0){
			setErrorZone("Nessuna credenziale inserita");
		}
		
		else if(nome.length() == 0 || descrizione.length() == 0 || sport.length() == 0 || materiali.size() == 0 
				|| prezzo.length() == 0){
			setErrorZone("Completare tutti i campi");
		}
		
		else if (controlCheck(cal, materiali, prezzo)) {
			frameSegreteria.setVisible(true);
		}
		
		else {
			frameSegreteria.getProxyListArticoli().addObject(new Articolo(nome, descrizione, sport, materiali,((Integer) code).toString(), prezzo, cal));
			
			panel.getCampoNome().setText("");
			panel.getCampoDescrizione().setText("");
			panel.getCampoSport().setText("");
			panel.getCampoMateriali().setText("");
			panel.getCampoPrezzo().setText("");
			panel.getListaMateriali().setListData(new String[0]);
			panel.getArrayListMateriali().removeAll(panel.getArrayListMateriali());
			panel.getDate().setDate(null);
			
			setErrorZone("Operazione completata");
			
			panel.updateTableArticoli();
			frameSegreteria.setVisible(true);
		}
	}
	
	private void confirmButtonPlusPressed() {
		if(((panel.getCampoMateriali().getText()).equalsIgnoreCase("")) == false) {
			panel.getArrayListMateriali().add(panel.getCampoMateriali().getText());
			String[] arrayStringMateriali = new String[panel.getArrayListMateriali().size()];
			panel.getArrayListMateriali().toArray(arrayStringMateriali);
			panel.getListaMateriali().setListData(arrayStringMateriali);
			panel.getCampoMateriali().setText("");
			frameSegreteria.setVisible(true);
		}
		else 
			setErrorZone("Materiale non inserito");
	}
	
	private void setErrorZone(String err) {
		panel.getErrorZone().setVisible(false);
		panel.getErrorZone().removeAll();
		JLabel error = new JLabel(err);
		error.setForeground(Color.RED);
		panel.getErrorZone().add(error);
		panel.getErrorZone().setVisible(true);
	}
	
	private boolean controlCheck (Calendar cal, AbstractList<String> materiali, String prezzo) {
		
		
		final int index = prezzo.indexOf(".");
		int resto = 1;
		
		try {
			resto = Integer.parseInt(prezzo.substring(index + 1, prezzo.length())) / 100;
		} 
		catch (NumberFormatException e2) {
			System.out.println("Il numero non è di tipo double");
		}
		
		for (String a : materiali) {
			if (a.matches("[a-zA-Z]+") == false) {
				setErrorZone("Materiale non valido");
				panel.getCampoMateriali().setText("");
				panel.getListaMateriali().setListData(new String[0]);
				panel.getArrayListMateriali().removeAll(panel.getArrayListMateriali());
				return true;
			}
		}
		if (cal == null || (new GregorianCalendar()).before(cal)) {
			setErrorZone("Data non valida");
			return true;
		}
		if ((prezzo.matches("[0-9]+") == false && 
				index == -1) || 
				resto != 0 ||
				(index != -1 && prezzo.substring(0, index).matches("[0-9]+") == false)) {
			setErrorZone("Prezzo non valido");
			panel.getCampoPrezzo().setText("");
			return true;
		}
		
		return false;
	
	}

}
