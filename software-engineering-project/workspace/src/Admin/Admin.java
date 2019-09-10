package Admin;
import java.awt.*;
import javax.swing.*;

import Login.LoginPage;
import MyList.MyList;
import MyList.MyListProxyFactory;

public class Admin extends JFrame {
	private static final String titolo = "Admin Page";
	
	private final JPanel loginZone = new JPanel(new BorderLayout());
	
	private final JPanel nord = new JPanel();
	private final JPanel panelTestoIniziale = new JPanel( new GridLayout(2,1));
	private static final JLabel testoIniziale = new JLabel("PAGINA DI AMMINISTRAZIONE DEL SISTEMA");

	private final JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel();
	private final JPanel errorZone = new JPanel();

	
	private final JPanel centroCentro = new JPanel(new GridLayout(8,1));
	private final JPanel panelTestoNome = new JPanel();
	private static final JLabel testoNome = new JLabel("Nome utente:"); 
	private final JPanel panelCampoNome = new JPanel();
	private static final JTextField campoNome = new JTextField(20);
	private final JPanel panelTestoPass = new JPanel();
	private static final JLabel testoPass = new JLabel("Password:");
	private final JPanel panelCampoPass = new JPanel();
	private static final JTextField campoPass = new JTextField(20);
	private final JPanel panelTestoUtente = new JPanel();
	private static final JLabel testoUtente = new JLabel("Tipo Utente:");
	//private static final JTextField campoUtente = new JTextField(20);
	private final JPanel panelCampoUtente = new JPanel();
	private static final String[] tipiUtenti = { "SegreteriaCatenaNegozi", "Magazziniere"};
	private static final JComboBox<String> campoUtente = new JComboBox<String>(tipiUtenti);

	private final JPanel centroSud = new JPanel();
	private final JButton bottoneConferma = new JButton("Conferma");
	private final JButton bottoneAnnulla = new JButton("Torna a LoginPage");
	
	private final JPanel est = new JPanel(new BorderLayout());
	
	private final JPanel estNord = new JPanel();
	private static final String[] columnNames = {"Nome Utente", "Tipo Utente", "Password"};
	private JTable table;
	
	private final JPanel estCentro = new JPanel();
	private final JButton bottoneRimuovi = new JButton("Rimuovi");
	
	private final JPanel sud = new JPanel();

	
	public Admin() {
		super(titolo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		this.setSize(800, 600);
		
		testoIniziale.setFont(new Font("Arial Black", Font.PLAIN, 20));
		panelTestoIniziale.add(testoIniziale);
		
		
		nord.add(panelTestoIniziale);
		
		campoNome.setPreferredSize(new Dimension(40, 30));
		campoPass.setPreferredSize(new Dimension(40, 30));

		panelTestoNome.add(testoNome);
		panelTestoPass.add(testoPass);
		panelCampoNome.add(campoNome);
		panelCampoPass.add(campoPass);
		panelTestoUtente.add(testoUtente);
		campoUtente.setSelectedIndex(0);
		panelCampoUtente.add(campoUtente);
		
		errorZone.setPreferredSize(new Dimension(200, 30));
		centroNord.add(errorZone);
		
		centroCentro.add(panelTestoNome);
		centroCentro.add(panelCampoNome);
		centroCentro.add(panelTestoPass);
		centroCentro.add(panelCampoPass);
		centroCentro.add(panelTestoUtente);
		centroCentro.add(panelCampoUtente);
		centroCentro.setPreferredSize(new Dimension(400, 300));
		
		
		centroSud.add(bottoneConferma);
		centroSud.add(bottoneAnnulla);
		
		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		centro.add(centroSud, BorderLayout.SOUTH);
		
		updateTable();
		estNord.setPreferredSize(new Dimension(600, 300));
		
		estCentro.add(bottoneRimuovi);
		
		est.add(estNord, BorderLayout.NORTH);
		est.add(estCentro, BorderLayout.SOUTH);
		
		loginZone.add(nord, BorderLayout.NORTH);
		loginZone.add(centro, BorderLayout.CENTER);
		loginZone.add(sud, BorderLayout.SOUTH);
		loginZone.add(new JPanel(), BorderLayout.WEST);
		loginZone.add(est, BorderLayout.EAST);
		
		Container frmContentPane = this.getContentPane();
		frmContentPane.add(loginZone, BorderLayout.CENTER);
		
		bottoneConferma.addActionListener(new AdminListener(this));
		bottoneAnnulla.addActionListener(new AdminListener(this));
		bottoneRimuovi.addActionListener(new AdminListener(this));
		campoNome.addKeyListener(new AdminListener(this));
		campoPass.addKeyListener(new AdminListener(this));
		this.addWindowListener(new AdminListener(this));
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void updateTable() {
		table = LoginPage.getProxyListUtenti().display(estNord, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(400, 250));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
	}
	
	public JTextField getCampoNome(){
		return campoNome;
	}
	
	public JTextField getCampoPass(){
		return campoPass;
	}
	
	public JComboBox<String> getCampoUtente(){
		return campoUtente;
	}

	public JPanel getErrorZone() {
		return errorZone;
	}

	public JTable getTable() {
		return table;
	}
}
