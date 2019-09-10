package InterfacciaResponsabileNegozio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import MainClass.Articolo;
import MainClass.ArticoloCount;
import MainClass.Ordine;

public class EntrataArticoliListener implements ActionListener, ListSelectionListener {
	private PanelEntrataArticoli panel;
	private Object[][] matrixArticleQuantity;
	
	public EntrataArticoliListener(PanelEntrataArticoli panel) {
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Aggiungi al negozio")) {
			if(panel.getTable().getSelectedRow() == -1) {
				setErrorZone1("Selezionare un ordine");
			}
			else if(panel.getTableArticoliOrdine().getSelectedRow() == -1) {
				setErrorZone1("Selezionare un articolo");
			}
			else if(((SpinnerNumberModel) panel.getCampoQuantità1().getModel()).getNumber().intValue() == 0) {
				setErrorZone1("Inserire quantità");
			}
			else{
				int campoQuantità = ((SpinnerNumberModel) panel.getCampoQuantità1().getModel()).getNumber().intValue();
				
				int indexOrder = panel.getTable().getSelectedRow();
				Ordine orderSelected = panel.getMapOrder().get(panel.getTable().getModel().getValueAt(indexOrder, 0));
				
				Articolo articleSelected = null;
				int indexArticle = panel.getTableArticoliOrdine().getSelectedRow();
				for(Articolo a: orderSelected.getQuantitàPerArticolo().keySet()) {
					if(panel.getTableArticoliOrdine().getModel().getValueAt(indexArticle, 1).equals(a.getCode()))
						articleSelected = a;
				}
				
				int quantitàArticolo = orderSelected.getQuantitàPerArticolo().get(articleSelected);
				
				if( campoQuantità > quantitàArticolo) {
					setErrorZone1("Quantità superiore agli articoli ordinati");
				}
				else {
					ArticoloCount articoloInNegozio = null;
					for(Object ac: panel.getFrame().getProxyListArticoliNegozio().getArrayList()) {
						if(ac instanceof ArticoloCount && ((ArticoloCount) ac).getArticolo().getCode().equals(articleSelected.getCode())) {
							articoloInNegozio = (ArticoloCount) ac;
						}
					}
					
					if(articoloInNegozio == null) {
						panel.getFrame().getProxyListArticoliNegozio().addObject(new ArticoloCount(articleSelected, campoQuantità));
					}
					else {
						panel.getFrame().getProxyListArticoliNegozio().removeObject(articoloInNegozio);
						articoloInNegozio.setCount(articoloInNegozio.getCount() + campoQuantità);
						panel.getFrame().getProxyListArticoliNegozio().addObject(articoloInNegozio);
					}
					panel.updateTableArticoliNegozio();
					
					panel.getTableArticoliOrdine().setVisible(false);
					panel.getTable().setVisible(false);
					
					panel.getFrame().getProxyListOrdineNegozio().removeObject(orderSelected);
					orderSelected.getQuantitàPerArticolo().remove(articleSelected);
					
					int numeroArticoliOrdine = Integer.parseInt((String) panel.getTable().getModel().getValueAt(indexOrder, 2));
					
					if(quantitàArticolo - campoQuantità > 0) {
						orderSelected.getQuantitàPerArticolo().put(articleSelected, quantitàArticolo - campoQuantità);
						panel.getFrame().getProxyListOrdineNegozio().addObject(orderSelected);
						
						panel.getTable().getModel().setValueAt(Integer.toString(numeroArticoliOrdine - campoQuantità), indexOrder, 2);
						
						panel.getTableArticoliOrdine().getModel().setValueAt(quantitàArticolo - campoQuantità, indexArticle, 3);
					}
					else if(quantitàArticolo - campoQuantità == 0 && orderSelected.getQuantitàPerArticolo().keySet().size() > 0){
						panel.getFrame().getProxyListOrdineNegozio().addObject(orderSelected);
						
						panel.getTable().getModel().setValueAt(Integer.toString(numeroArticoliOrdine - campoQuantità), indexOrder, 2);
						
						matrixArticleQuantity = new Object[orderSelected.getQuantitàPerArticolo().keySet().size()][4];
						
						int i = 0;
						for(Articolo a: orderSelected.getQuantitàPerArticolo().keySet()) {
							matrixArticleQuantity[i][0] = a.getNome();
							matrixArticleQuantity[i][1] = a.getCode();
							matrixArticleQuantity[i][2] = a.getPrezzo();
							matrixArticleQuantity[i][3] = orderSelected.getQuantitàPerArticolo().get(a);
							i++;
						}
						
						panel.updateTableArticoliOrdine(matrixArticleQuantity);
					}
					else {
						panel.updateTableOrdini();
						panel.updateTableArticoliOrdine(new Object[0][4]);
					}
					
					panel.getTableArticoliOrdine().setVisible(true);
					panel.getTable().setVisible(true);
					panel.getFrame().setVisible(true);
				}
			}
		}
		else if(button.getText().equals("Vendi articolo")) {
			if(panel.getTableArticoliNegozio().getSelectedRow() == -1) {
				setErrorZone2("Seleziona un articolo");
			}
			else {
				ArticoloCount articleSelected = null;
				int index = panel.getTableArticoliNegozio().getSelectedRow();
				for(Object a: panel.getFrame().getProxyListArticoliNegozio().getArrayList()) {
					if(a instanceof ArticoloCount && ((ArticoloCount) a).getArticolo().getCode().equals(panel.getTableArticoliNegozio().getModel().getValueAt(index, 5))) {
						articleSelected = (ArticoloCount) a;
					}
				}
				
				int campoQuantità = ((SpinnerNumberModel) panel.getCampoQuantità2().getModel()).getNumber().intValue();
				int quantitàArticolo = articleSelected.getCount();
				
				
				
				if(campoQuantità > quantitàArticolo) {
					setErrorZone2("Non hai abbastanza articoli in negozio");
				}
				else {
					panel.getFrame().setVisible(false);
					panel.getTableArticoliNegozio().setVisible(false);
					
					setErrorZone2("Vendita completata");
					panel.getFrame().getProxyListArticoliNegozio().removeObject(articleSelected);
					if(quantitàArticolo - campoQuantità > 0) {
						articleSelected.setCount(quantitàArticolo - campoQuantità);
						panel.getFrame().getProxyListArticoliNegozio().addObject(articleSelected);
						panel.getTableArticoliNegozio().getModel().setValueAt(Integer.toString(quantitàArticolo - campoQuantità), index, 2);
					}
					else {
						panel.updateTableArticoliNegozio();
					}
					
					panel.getFrame().setVisible(true);
					panel.getTableArticoliNegozio().setVisible(true);
				}
				
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

	
	public void valueChanged(ListSelectionEvent e) {
		int index = panel.getTable().getSelectedRow();
		
		if(index != -1) {
			Ordine selectedOrdine = panel.getMapOrder().get(panel.getTable().getModel().getValueAt(index, 0));
			matrixArticleQuantity = new Object[selectedOrdine.getQuantitàPerArticolo().keySet().size()][4];
			
			int i = 0;
			for(Articolo a: selectedOrdine.getQuantitàPerArticolo().keySet()) {
				matrixArticleQuantity[i][0] = a.getNome();
				matrixArticleQuantity[i][1] = a.getCode();
				matrixArticleQuantity[i][2] = a.getPrezzo();
				matrixArticleQuantity[i][3] = selectedOrdine.getQuantitàPerArticolo().get(a);
				i++;
			}
			
			panel.updateTableArticoliOrdine(matrixArticleQuantity);

			panel.getFrame().setVisible(true);
		}
	}

}
