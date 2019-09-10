package InterfacciaResponsabileNegozio;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Login.LoginPage;

public class ResponsabileNegozioListener implements WindowListener, ChangeListener, MouseListener {
	FrameResponsabileNegozio frame;
	
	public ResponsabileNegozioListener(FrameResponsabileNegozio frame) {
		this.frame = frame;
	}
	
	public void windowOpened(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		frame.getPanelOrdinaArticoli().getProxyListArticolo().saveData();
		frame.getProxyListOrdine().saveData();
		frame.getProxyListOrdineNegozio().saveData();
		frame.getProxyListArticoliNegozio().saveData();
		LoginPage.getLoginPage().setVisible(true);
	}

	public void windowClosed(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}

	public void stateChanged(ChangeEvent e) {
		JTabbedPane pane = (JTabbedPane) e.getSource();
		int i = pane.getSelectedIndex();
		
		if( i == 1) {
			frame.getPanelListaOrdini().updateTableOrdini();
		}
		else if( i == 2) {
			frame.getPanelEntrataArticoli().updateTableOrdini();
		}
		
		frame.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		JTabbedPane pane = (JTabbedPane) e.getSource();
		int i = pane.getSelectedIndex();
		
		if( i == 2) {
			frame.getPanelEntrataArticoli().updateTableOrdini();
		}
		
		frame.setVisible(true);
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}


}
