package InterfacciaResponsabileNegozio;

import java.awt.*;
import javax.swing.*;

import Login.LoginPage;
import MainClass.Negozio;
import MyList.MyList;
import MyList.MyListProxyFactory;
import MyList.MyListRealFactory;
import Utenti.Utente;

public class FrameResponsabileNegozio extends JFrame {
	private Negozio negozio;
	
	private final MyList proxyListOrdineNegozio;
	private final MyList proxyListArticoloNegozio;
	
	private static final String titolo = "ResponsabileNegozio Page";
	
	private final JPanel nord = new JPanel();
	private final JLabel benvenuto = new JLabel("Responsabile Negozio");
	
	private PanelOrdinaArticoli panelOrdinaArticoli;
	
	private PanelListaOrdini panelListaOrdini;
	
	private PanelEntrataArticoli panelEntrataArticoli;
	
	private final JTabbedPane tabbedPane = new JTabbedPane();
	
	public FrameResponsabileNegozio(Utente user) {
		super(titolo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		this.setSize(1300, 700);
		
		for(Object n: LoginPage.getProxyListNegozi().getArrayList()) {
			if(n instanceof Negozio && ((Negozio) n).getUser().equals(user))
				this.negozio = (Negozio) n;
		}
		
		proxyListArticoloNegozio = new MyListRealFactory("ArticleNegozioDataBase", negozio.getUser().getUsername());
		proxyListOrdineNegozio = new MyListRealFactory("OrderNegozioDataBase", negozio.getUser().getUsername());
		
		benvenuto.setText(benvenuto.getText() + " - " + negozio.getUser().getUsername() + " - " + negozio.getNomeCatena() + " " + negozio.getIndirizzo());
		benvenuto.setFont(new Font("Arial Black", Font.PLAIN, 20));
		//benvenuto.setPreferredSize(new Dimension(269, 50));
		nord.add(benvenuto);
		
		panelOrdinaArticoli = new PanelOrdinaArticoli(negozio, this);
        tabbedPane.addTab("Nuovo Ordine", null, panelOrdinaArticoli, null);
        
        panelListaOrdini = new PanelListaOrdini(negozio, this);
        tabbedPane.addTab("Visualizza Ordini", null, panelListaOrdini, null);
        
        panelEntrataArticoli = new PanelEntrataArticoli(this);
        tabbedPane.addTab("Entrata Articoli", null, panelEntrataArticoli, null);
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
        tabbedPane.setForeground(new Color(0, 0, 153));
        nord.setBackground(new Color(250, 251, 232));
        
		Container frmContentPane = this.getContentPane();
		frmContentPane.add(nord, BorderLayout.NORTH);
		frmContentPane.add(tabbedPane, BorderLayout.CENTER);
		frmContentPane.setBackground(new Color(250, 251, 232));
		
		tabbedPane.addChangeListener(new ResponsabileNegozioListener(this));
		this.addWindowListener(new ResponsabileNegozioListener(this));
		tabbedPane.addMouseListener(new ResponsabileNegozioListener(this));
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public MyList getProxyListOrdine(){
		return LoginPage.getProxyListOrdini();
	}
	
	public PanelOrdinaArticoli getPanelOrdinaArticoli() {
		return panelOrdinaArticoli;
	}
	
	public PanelListaOrdini getPanelListaOrdini() {
		return panelListaOrdini;
	}
	
	public PanelEntrataArticoli getPanelEntrataArticoli() {
		return panelEntrataArticoli;
	}
	
	public MyList getProxyListOrdineNegozio() {
		return proxyListOrdineNegozio;
	}
	
	public MyList getProxyListArticoliNegozio() {
		return proxyListArticoloNegozio;
	}

}
