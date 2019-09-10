package InterfacciaSegreteriaCatenaNegozi;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import MainClass.Articolo;
import MainClass.ArticoloCount;
import MainClass.Ordine;

public class MovimentiListener implements ActionListener, ListSelectionListener {
	
	private PanelMovimenti panel;
	private FrameSegreteriaCatenaNegozi frame;
	private Object[][] matrixArticleQuantity;
	
	public MovimentiListener(PanelMovimenti panel,
			FrameSegreteriaCatenaNegozi frame) {
		this.panel = panel;
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Mostra per Ordine")) {
			frame.getProxyListOrdini().displayForOrder(panel.getTableMovimenti(), panel.getCentroOrdini(), panel.getColumnNames());
		}
		else if(button.getText().equals("Mostra per Negozio")) {
			frame.getProxyListOrdini().displayForNegozio(panel.getTableMovimenti(), panel.getCentroOrdini(), panel.getColumnNames());
		}
		
		panel.updateTableArticoliOrdine(new Object[0][8]);
		frame.setVisible(true);
	}
	
	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	private void setErrorZone(String err) {
		panel.getErrorZone().removeAll();
		JLabel error = new JLabel(err);
		error.setForeground(Color.RED);
		panel.getErrorZone().add(error);
		frame.setVisible(true);
	}

	
	public void valueChanged(ListSelectionEvent e) {
		int index = panel.getTableMovimenti().getSelectedRow();
		
		if(index != -1) {
			Ordine selectedOrdine = panel.getMapOrder().get(panel.getTableMovimenti().getModel().getValueAt(index, 0));
			matrixArticleQuantity = new Object[selectedOrdine.getQuantitàPerArticolo().keySet().size()][4];
			
			int i = 0;
			for(Articolo a: selectedOrdine.getQuantitàPerArticolo().keySet()) {
				ArticoloCount articoloCount = new ArticoloCount(a, selectedOrdine.getQuantitàPerArticolo().get(a));
				matrixArticleQuantity[i] = articoloCount.getArrayString();
				i++;
			}
			
			panel.updateTableArticoliOrdine(matrixArticleQuantity);

			frame.setVisible(true);
		}
	}

}
