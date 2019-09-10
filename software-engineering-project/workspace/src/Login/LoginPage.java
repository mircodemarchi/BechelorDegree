package Login;
import java.awt.*;
import javax.swing.*;

import MyList.MyList;
import MyList.MyListProxyFactory;

public class LoginPage extends JFrame {
	private static LoginPage loginPage = null;
	
	private static final MyList proxyListArticoli = new MyListProxyFactory("ArticleDataBase");
	private static final MyList proxyListNegozi = new MyListProxyFactory("NegoziDataBase");
	private static final MyList proxyListOrdini = new MyListProxyFactory("OrderDataBase");
	private static final MyList proxyListUtenti = new MyListProxyFactory("UserDataBase");
	
	private static final String    titolo = "Gestore di una Catena di Negozi - Login Page - Prototipo Software";
	private static final String    path = "/Users/mircodemarchi/Desktop/EclipseWorkspace/IngegneriaDelSoftware/bin/";
	
	private static final ImageIcon iconImage =    new ImageIcon(path + "logounivr1.jpeg");
	
	private static final JPanel    top =          new JPanel(new GridLayout(1, 2));
	private static final JLabel    headerImage =  new JLabel(new ImageIcon(path + "logounivr2.png")); 
	private static final JLabel    versione =     new JLabel("Prototipo"); 
	
	private static final JPanel    left =         new JPanel();
	
	private static final JPanel    mainPanel =    new JPanel(new BorderLayout());
	
	private static final JLabel    titoloLabel =  new JLabel("Gestore di una Catena di Negozi");
	private static final JPanel    nord =         new JPanel(new FlowLayout(FlowLayout.LEFT));
	
	private static final JPanel    ovest =        new JPanel();
	
	private static final JPanel    centro =       new JPanel(new BorderLayout());
	
	private static final JPanel centroNord = new JPanel(new GridLayout(2,1));
	private final JPanel errorZone = new JPanel();
	private static final JLabel testoAccedi = new JLabel("Accedi al sistema"); 
	
	private static final JPanel loginZone = new JPanel(new GridLayout(2,2));
	private static final JPanel panelTestoNome = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel testoNome = new JLabel("Nome utente"); 
	private static final JPanel panelCampoNome = new JPanel();
	private static final JTextField campoNome = new JTextField(20);
	private static final JPanel panelTestoPass = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel testoPass = new JLabel("Password"); 
	private static final JPanel panelCampoPass = new JPanel();
	private static final JPasswordField campoPass = new JPasswordField(20);
	
	private static final JPanel    buttonZone = new JPanel();
	private static final JButton   bottoneConferma = new JButton("Conferma");
	private static final JButton   bottoneAnnulla = new JButton("Shutdown");
	
	
	private static final JPanel    est =          new JPanel();
	
	private static final JPanel    sud =          new JPanel();
	
	private static final JPanel    right =        new JPanel();
	
	private static final JPanel    bottom =          new JPanel(new GridLayout(1, 2));
	private static final JLabel    sviluppatori = new JLabel("Sviluppatori: Guadagnini Filippo, De Marchi Mirco"); 
	private static final JLabel    anno =         new JLabel("2018");
	
	private LoginPage() { 
		super(titolo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		this.setIconImage(iconImage.getImage());
		//this.setSize(1000, 600);
		
		top.add(headerImage);
		versione.setVerticalAlignment(JLabel.TOP);
		versione.setHorizontalAlignment(JLabel.RIGHT);
		versione.setFont(new Font("Arial", Font.PLAIN, 13));
		top.add(versione);
		
		left.setPreferredSize(new Dimension(70, 200));
		
		nord.setPreferredSize(new Dimension(500, 125));
		titoloLabel.setFont(new Font("Arial Black", Font.PLAIN, 30));
		nord.add(titoloLabel);
		
		ovest.setPreferredSize(new Dimension(200, 200));
		
		errorZone.setPreferredSize(new Dimension(150, 20));
		centroNord.add(errorZone);
		testoAccedi.setFont(new Font("Arial", Font.BOLD, 25));
		testoAccedi.setHorizontalAlignment(JLabel.CENTER);
		centroNord.add(testoAccedi);
		
		testoNome.setFont(new Font("Arial", Font.PLAIN, 18));
		testoPass.setFont(new Font("Arial", Font.PLAIN, 18));
		panelTestoNome.add(testoNome);
		panelTestoPass.add(testoPass);
		campoNome.setPreferredSize(new Dimension(150, 35));
		campoPass.setPreferredSize(new Dimension(150, 35));
		panelCampoNome.add(campoNome);
		panelCampoPass.add(campoPass);
		
		loginZone.add(panelTestoNome);
		loginZone.add(panelCampoNome);
		loginZone.add(panelTestoPass);
		loginZone.add(panelCampoPass);
		
		buttonZone.add(bottoneConferma);
		buttonZone.add(bottoneAnnulla);
		
		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(loginZone, BorderLayout.CENTER);
		centro.add(buttonZone, BorderLayout.SOUTH);
		centro.setPreferredSize(new Dimension(550, 200));
		centro.setBorder( BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3), BorderFactory.createRaisedBevelBorder()));
		
		est.setPreferredSize(new Dimension(200, 200));
		
		sud.setPreferredSize(new Dimension(500, 150));
		
		right.setPreferredSize(new Dimension(70, 250));
		
		anno.setHorizontalAlignment(JLabel.LEFT);
		bottom.add(anno);
		sviluppatori.setHorizontalAlignment(JLabel.RIGHT);
		bottom.add(sviluppatori);
		bottom.setBorder(BorderFactory.createLineBorder(Color.black));
		
		top.setBackground(new Color(248, 255, 121));
		left.setBackground(new Color(250, 251, 232));
		nord.setBackground(new Color(250, 251, 232));
		ovest.setBackground(new Color(250, 251, 232));
		est.setBackground(new Color(250, 251, 232));
		sud.setBackground(new Color(250, 251, 232));
		right.setBackground(new Color(250, 251, 232));
		bottom.setBackground(new Color(248, 255, 121));
		
		mainPanel.add(nord, BorderLayout.NORTH);
		mainPanel.add(ovest, BorderLayout.WEST);
		mainPanel.add(centro, BorderLayout.CENTER);
		mainPanel.add(est, BorderLayout.EAST);
		mainPanel.add(sud, BorderLayout.SOUTH);
		
		Container frmContentPane = this.getContentPane();
		frmContentPane.add(top, BorderLayout.NORTH);
		frmContentPane.add(left, BorderLayout.WEST);
		frmContentPane.add(mainPanel, BorderLayout.CENTER);
		frmContentPane.add(right, BorderLayout.EAST);
		frmContentPane.add(bottom, BorderLayout.SOUTH);
		
		bottoneConferma.addActionListener(new LoginPageListener(this));
		bottoneAnnulla.addActionListener(new LoginPageListener(this));
		campoNome.addKeyListener(new LoginPageListener(this));
		campoPass.addKeyListener(new LoginPageListener(this));
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public static LoginPage getLoginPage() {
		if(loginPage == null) {
			loginPage = new LoginPage();
		}
		
		return loginPage;
	}
	
	public static MyList getProxyListArticoli() {
		return proxyListArticoli;
	}
	
	public static MyList getProxyListNegozi() {
		return proxyListNegozi;
	}
	
	public static MyList getProxyListOrdini() {
		return proxyListOrdini;
	}
	
	public static MyList getProxyListUtenti() {
		return proxyListUtenti;
	}
	
	public JTextField getCampoNome() {
		return campoNome;
	}
	
	public JPasswordField getCampoPass() {
		return campoPass;
	}
	
	public JPanel getErrorZone() {
		return errorZone;
	}
	
	public static void main(String[] args) {
		/*
		Negozio n = new Negozio("2312", new ResponsabileNegozio("Mirco", "admin"), "MioNegozio", "Villafranca"); 
		MyList l = new MyListProxyFactory("NegoziDataBase");
		l.addObject(n);
		l.saveData();*/
		
		LoginPage.getLoginPage().setVisible(true);
	}
}
