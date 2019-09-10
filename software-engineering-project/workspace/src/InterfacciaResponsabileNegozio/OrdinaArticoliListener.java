package InterfacciaResponsabileNegozio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import MainClass.Articolo;
import MainClass.Ordine;

public class OrdinaArticoliListener implements ActionListener{
	private PanelOrdinaArticoli panel;
	private static Ordine ordine;
	private Object[][] matrixArticleQuantity;
	
	public OrdinaArticoliListener(PanelOrdinaArticoli panel) {
		this.panel = panel;
		ordine = null;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Aggiungi a ordine")) {
			if(panel.getTable().getSelectedRow() == -1) {
				setErrorZone1("Selezionare un articolo");
				setErrorZone2("");
			}
			else if( ((SpinnerNumberModel) panel.getCampoQuantità().getModel()).getNumber().intValue() == 0) {
				setErrorZone1("Inserire quantità");
				setErrorZone2("");
			}
			else {
				setErrorZone1("Articolo aggiunto con successo");
				setErrorZone2("");
				
				if(ordine == null) 
					ordine = new Ordine(panel.getNegozio());
				
				Articolo selectedArticolo = panel.getMapArticle().get(panel.getTable().getModel().getValueAt(panel.getTable().getSelectedRow(),4));
				panel.getSetArticolo().add(selectedArticolo);
				matrixArticleQuantity = new Object[panel.getSetArticolo().size()][4];
				
				ordine.put(selectedArticolo, ((SpinnerNumberModel) panel.getCampoQuantità().getModel()).getNumber().intValue());
				
				int i = 0;
				for(Articolo a: panel.getSetArticolo()) {
					if(i < panel.getSetArticolo().size()) {
						matrixArticleQuantity[i][0] = a.getNome();
						matrixArticleQuantity[i][1] = a.getCode();
						matrixArticleQuantity[i][2] = a.getPrezzo();
						matrixArticleQuantity[i][3] = ordine.getQuantitàPerArticolo().get(a);
						i++;
					}
				}
				panel.updateTableArticoliOrdine(matrixArticleQuantity);
				
				panel.getPrezzo().setText("Totale: " + ordine.getPrezzo() + "€");
				panel.setVisible(true);
			}
		}
		else if(button.getText().equals("Conferma Ordine")) {
			if(panel.getSetArticolo().size() == 0) {
				setErrorZone2("Inserire un articolo");
				setErrorZone1("");
			}
			else {
				setErrorZone1("");
				
				panel.getFrame().getProxyListOrdine().addObject(new Ordine(ordine));
				panel.getFrame().getProxyListOrdineNegozio().addObject(new Ordine(ordine));
				panel.getSetArticolo().removeAll(panel.getSetArticolo());
				matrixArticleQuantity = new Object[panel.getSetArticolo().size()][2];
				panel.updateTableArticoliOrdine(matrixArticleQuantity);
				
				setErrorZone2("Ordine confermato");
				panel.setVisible(true);
				ordine = null;
			}
		}
		else if(button.getText().equals("Rimuovi da ordine")) {
			if(panel.getTableArticoliOrdine().getSelectedRow() == -1) {
				setErrorZone2("Selezionare un articolo");
				setErrorZone1("");
			}
			else {
				setErrorZone1("");
				setErrorZone2("Articolo rimosso");
				
				Articolo selectedArticolo = panel.getMapArticle().get(panel.getTableArticoliOrdine().getModel().getValueAt(panel.getTableArticoliOrdine().getSelectedRow(),1));
				panel.getSetArticolo().remove(selectedArticolo);
				matrixArticleQuantity = new Object[panel.getSetArticolo().size()][4];
				
				ordine.remove(selectedArticolo);
				
				int i = 0;
				for(Articolo a: panel.getSetArticolo()) {
					if(i < panel.getSetArticolo().size()) {
						matrixArticleQuantity[i][0] = a.getNome();
						matrixArticleQuantity[i][1] = a.getCode();
						matrixArticleQuantity[i][2] = a.getPrezzo();
						matrixArticleQuantity[i][3] = ordine.getQuantitàPerArticolo().get(a);
						i++;
					}
				}
				panel.updateTableArticoliOrdine(matrixArticleQuantity);
				
				panel.getPrezzo().setText("Prezzo: " + ordine.getPrezzo() + "€");
				panel.setVisible(true);
			}
		}
		
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
	
	
}
