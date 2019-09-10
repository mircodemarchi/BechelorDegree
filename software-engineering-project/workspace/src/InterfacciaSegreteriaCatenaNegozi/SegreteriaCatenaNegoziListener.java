package InterfacciaSegreteriaCatenaNegozi;
import java.awt.Color;

import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import MainClass.Articolo;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import InterfacciaResponsabileNegozio.FrameResponsabileNegozio;
import Login.LoginPage;


public class SegreteriaCatenaNegoziListener implements WindowListener, ChangeListener{
	private FrameSegreteriaCatenaNegozi frame;
	
	public SegreteriaCatenaNegoziListener(FrameSegreteriaCatenaNegozi frame) {
		this.frame = frame;
	}
	
	public void windowOpened(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		frame.getProxyListArticoli().saveData();
		frame.getProxyListNegozi().saveData();
		frame.getProxyListUsers().saveData();
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
			frame.getPanelNewNegozio().updateTableNegozi();
		}
		else if( i == 2) {
			frame.getPanelNewNegozio().updateTableNegozi();
		}
		
		frame.setVisible(true);
	}

}
