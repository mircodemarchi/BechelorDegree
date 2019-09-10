package InterfacciaSegreteriaCatenaNegozi;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.toedter.calendar.JCalendar;

import InterfacciaResponsabileNegozio.PanelEntrataArticoli;
import InterfacciaResponsabileNegozio.PanelListaOrdini;
import InterfacciaResponsabileNegozio.PanelOrdinaArticoli;
import InterfacciaResponsabileNegozio.ResponsabileNegozioListener;
import Login.LoginPage;
import MainClass.Negozio;
import MyList.MyList;
import MyList.MyListProxyFactory;
import Utenti.Utente;

public class FrameSegreteriaCatenaNegozi extends JFrame {
	private static final String titolo = "SegreteriaCatenaNegozi Page";
	
	private final JPanel nord = new JPanel();
	private final JLabel benvenuto = new JLabel("Segreteria Catena Negozio");
	
	private PanelMovimenti panelMovimenti;
	
	private PanelNewArticolo panelNewArticolo;
	
	private PanelNewNegozio panelNewNegozio;
	
	private final JTabbedPane tabbedPane = new JTabbedPane();
	
	public FrameSegreteriaCatenaNegozi() {
		super(titolo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		this.setSize(1300, 700);
		
		
		benvenuto.setFont(new Font("Arial Black", Font.PLAIN, 20));
		
		nord.add(benvenuto);
		
		panelNewArticolo = new PanelNewArticolo(this);
        tabbedPane.addTab("Nuovo Articolo", null, panelNewArticolo, null);
        
        panelNewNegozio = new PanelNewNegozio(this);
        tabbedPane.addTab("Nuovo Negozio", null, panelNewNegozio, null);
        
		panelMovimenti = new PanelMovimenti(this);
        tabbedPane.addTab("Lista Movimenti", null, panelMovimenti, null);
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
        tabbedPane.setForeground(new Color(0, 0, 153));
        nord.setBackground(new Color(250, 251, 232));
        
		Container frmContentPane = this.getContentPane();
		frmContentPane.add(nord, BorderLayout.NORTH);
		frmContentPane.add(tabbedPane, BorderLayout.CENTER);
		frmContentPane.setBackground(new Color(250, 251, 232));
		
		tabbedPane.addChangeListener(new SegreteriaCatenaNegoziListener(this));
		this.addWindowListener(new SegreteriaCatenaNegoziListener(this));
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public MyList getProxyListArticoli(){
		return LoginPage.getProxyListArticoli();
	}
	
	public MyList getProxyListNegozi(){
		return LoginPage.getProxyListNegozi();
	}
	
	public MyList getProxyListOrdini(){
		return LoginPage.getProxyListOrdini();
	}
	
	public PanelNewArticolo getPanelNewArticolo() {
		return panelNewArticolo;
	}
	
	public PanelMovimenti getPanelMovimenti() {
		return panelMovimenti;
	}
	
	public MyList getProxyListUsers() {
		return LoginPage.getProxyListUtenti();
	}
	
	
	public PanelNewNegozio getPanelNewNegozio() {
		return panelNewNegozio;
	}

}
