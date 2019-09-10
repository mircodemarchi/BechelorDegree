package InterfacciaResponsabileNegozio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JCalendar;

import MainClass.Articolo;
import MainClass.ArticoloCount;
import MainClass.Ordine;

public class ListaOrdiniListener implements ActionListener, ListSelectionListener {
	private PanelListaOrdini panel;
	private Object[][] matrixArticleQuantity;
	
	public ListaOrdiniListener(PanelListaOrdini panel) {
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		panel.updateTableArticoliOrdine(new Object[0][8]);
		
		if(button.getText().equals("Ricerca Ordine")) {
			int n = panel.updateTableOrdini((Integer) panel.getCampoNumero().getValue());
			
			if(n == 0)
				setErrorZone1("Ordine " + (Integer) panel.getCampoNumero().getValue() + ": nessun elemento");
			else
				setErrorZone1("Ordine " + (Integer) panel.getCampoNumero().getValue() + ": elemento trovato");
			
			setErrorZone2("");
		}
		else if(button.getText().equals("Filtra per date")) {
			Calendar dataInizio = panel.getCalendarInizio().getCalendar();
			Calendar dataFine = panel.getCalendarFine().getCalendar();
			
			if( dataInizio == null && dataFine == null) {
				setErrorZone2("Pattern non riconosciuto per le date");
				setErrorZone1("");
				panel.updateTableOrdini();
			}
			else if( dataInizio == null){
				setErrorZone2("Pattern non riconosciuto per Data Inizio");
				setErrorZone1("");
				panel.updateTableOrdini();
			}
			else if( dataFine == null){
				setErrorZone2("Pattern non riconosciuto per Data Fine");
				setErrorZone1("");
				panel.updateTableOrdini();
			}
			else if( (new GregorianCalendar()).before(dataInizio) || (new GregorianCalendar()).before(dataFine)){
				setErrorZone2("Non inserire date che superano la data di oggi");
				setErrorZone1("");
				panel.updateTableOrdini();
			}
			else if( dataInizio.after(dataFine)) {
				setErrorZone2("Data inizio deve venire prima di Data fine");
				setErrorZone1("");
				panel.updateTableOrdini();
			}
			else {
				setErrorZone2("Ordini da " + dataInizio.get(Calendar.DAY_OF_MONTH) + '/' + (int)(dataInizio.get(Calendar.MONTH) + 1) + '/' + dataInizio.get(Calendar.YEAR) +
								" a " + dataFine.get(Calendar.DAY_OF_MONTH) + '/' + (int)(dataFine.get(Calendar.MONTH) + 1) + '/' + dataFine.get(Calendar.YEAR));
				setErrorZone1("");
				panel.updateTableOrdini(dataInizio, dataFine);
			}
			
		}
		else if(button.getText().equals("Ripristina tutti gli ordini")) {
			panel.updateTableOrdini();
		}
		
		
		panel.getFrame().setVisible(true);

	}
	
	private void setErrorZone1(String err) {
		panel.getErrorZone1().setVisible(false);
		panel.getErrorZone1().removeAll();
		JLabel error = new JLabel(err);
		error.setForeground(Color.RED);
		panel.getErrorZone1().add(error);
		panel.getErrorZone1().setVisible(true);
	}
	
	private void setErrorZone2(String err) {
		panel.getErrorZone2().setVisible(false);
		panel.getErrorZone2().removeAll();
		JLabel error = new JLabel(err);
		error.setForeground(Color.RED);
		panel.getErrorZone2().add(error);
		panel.getErrorZone2().setVisible(true);
	}

	
	public void valueChanged(ListSelectionEvent e) {
		int index = panel.getTable().getSelectedRow();
		
		if(index != -1) {
			Ordine selectedOrdine = panel.getMapOrder().get(panel.getTable().getModel().getValueAt(index, 0));
			matrixArticleQuantity = new Object[selectedOrdine.getQuantitàPerArticolo().keySet().size()][8];
			
			int i = 0;
			for(Articolo a: selectedOrdine.getQuantitàPerArticolo().keySet()) {
				ArticoloCount articoloCount = new ArticoloCount(a, selectedOrdine.getQuantitàPerArticolo().get(a));
				matrixArticleQuantity[i] = articoloCount.getArrayString();
				i++;
			}
			
			panel.updateTableArticoliOrdine(matrixArticleQuantity);

			panel.getFrame().setVisible(true);
		}
	}

	
	

}
