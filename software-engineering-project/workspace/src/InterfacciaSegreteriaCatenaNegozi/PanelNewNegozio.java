package InterfacciaSegreteriaCatenaNegozi;
import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import MyList.MyList;
import MyList.MyListProxyFactory;

public class PanelNewNegozio extends JPanel {
	
	private FrameSegreteriaCatenaNegozi segreteriaCatenaNegoziFrame;
	
	private static final String titolo = "New Store Page";
	
	private final JPanel nord = new JPanel();
	
	private final JPanel ovest = new JPanel();
	
	private final JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel ();
	private static final JLabel lista = new JLabel("Lista dei Negozi in DataBase");
	
	private final JPanel centroCentro = new JPanel ();

	private ArrayList<String> columnNamesNegozi = new ArrayList<String>();
	private JTable tableNegozi = new JTable();
	private TableModel tableModelNegozi = new DefaultTableModel() {
	    public boolean isCellEditable(int row, int column) {
	       return false;
	    }
	};
	
	private final JPanel centroSud = new JPanel();
	private final JButton remove =new JButton("Rimuovi Negozio"); 
	
	private final JPanel est = new JPanel(new BorderLayout());
	
	private final JPanel estNord = new JPanel(new GridLayout(2,1));
	private final JPanel errorZone = new JPanel();
	
	private final JPanel estCentro = new JPanel (new GridLayout(10,1));
	private static final JLabel testoNome = new JLabel("Nome:"); 
	private final JPanel panelCampoNome = new JPanel();
	private final JTextField campoNome = new JTextField(25);
	private static final JLabel testoIndirizzo = new JLabel("Indirizzo:"); 
	private final JPanel panelCampoIndirizzo = new JPanel();
	private static final JTextField campoIndirizzo = new JTextField(25);
	private final JLabel testoCity = new JLabel("Città:"); 
	private final JPanel panelCampoCity = new JPanel();
	private final JTextField campoCity = new JTextField(25);
	
	private static final JLabel testoNewResponsabile = new JLabel("Responsabile:"); 
	private final JPanel panelNewResponsabile = new JPanel();
	private static final JLabel testoUtente = new JLabel("Username:");
	private final JTextField campoUtente = new JTextField(10);
	private static final JLabel testoPass = new JLabel("Password:");
	private final JTextField campoPass = new JTextField(10);

	private final JPanel estEst = new JPanel ();
	
	private final JPanel estSud = new JPanel();
	private final JButton add = new JButton("Aggiungi Negozio"); 
	
	private final JPanel sud = new JPanel();
	
	public PanelNewNegozio(FrameSegreteriaCatenaNegozi segreteriaCatenaNegoziFrame) {
		
		super(new BorderLayout());
		this.segreteriaCatenaNegoziFrame = segreteriaCatenaNegoziFrame;

		
		fillColumnsModelNegozi(columnNamesNegozi, tableModelNegozi);
		
		nord.add(lista);
		
		updateTableNegozi();
		
		centroNord.add(lista);
		centroSud.add(remove);

		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		centro.add(centroSud, BorderLayout.SOUTH);
		

		errorZone.setPreferredSize(new Dimension(150, 30));
		estNord.add(errorZone);
		
		panelCampoNome.add(campoNome);
		panelCampoIndirizzo.add(campoIndirizzo);	
		panelCampoCity.add(campoCity);	
		panelNewResponsabile.add(testoUtente);
		panelNewResponsabile.add(campoUtente);
		panelNewResponsabile.add(testoPass);
		panelNewResponsabile.add(campoPass);
		

		estCentro.add(testoNome);
		estCentro.add(panelCampoNome);
		estCentro.add(testoIndirizzo);
		estCentro.add(panelCampoIndirizzo);
		estCentro.add(testoCity);
		estCentro.add(panelCampoCity);
		estCentro.add(testoNewResponsabile);
		estCentro.add(panelNewResponsabile);
		
		estCentro.setPreferredSize(new Dimension(450, 700));
		
		estSud.add(add);
		
		est.add(estNord, BorderLayout.NORTH);
		est.add(estCentro, BorderLayout.CENTER);
		est.add(estSud, BorderLayout.SOUTH);
		est.add(estEst, BorderLayout.EAST);
		
		nord.setBackground(Color.WHITE);
		ovest.setBackground(Color.WHITE);
		centroNord.setBackground(Color.WHITE);
		centroCentro.setBackground(Color.WHITE);
		centroSud.setBackground(Color.WHITE);
		estNord.setBackground(Color.WHITE);
		errorZone.setBackground(Color.WHITE);
		estCentro.setBackground(Color.WHITE);

		panelCampoNome.setBackground(Color.WHITE);
		panelCampoIndirizzo.setBackground(Color.WHITE);
		panelCampoCity.setBackground(Color.WHITE);
		estEst.setBackground(Color.WHITE);
		estSud.setBackground(Color.WHITE);
		sud.setBackground(Color.WHITE);
		panelNewResponsabile.setBackground(Color.WHITE);
		
	
		this.add(nord, BorderLayout.NORTH);
		this.add(est, BorderLayout.EAST);
		this.add(centro, BorderLayout.CENTER);
		this.add(ovest, BorderLayout.WEST);
		this.add(sud, BorderLayout.SOUTH);
		
		add.addActionListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		remove.addActionListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));

		campoNome.addKeyListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		campoIndirizzo.addKeyListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		campoCity.addKeyListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		campoUtente.addKeyListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		campoPass.addKeyListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		add.addKeyListener(new NewNegozioListener(this, segreteriaCatenaNegoziFrame));
		

		


	}
	
	public void updateTableNegozi() {
		segreteriaCatenaNegoziFrame.getProxyListNegozi().displayNegozi(tableModelNegozi, tableNegozi, centroCentro);
	}
	
	private void fillColumnsModelNegozi (ArrayList<String> columnNames, TableModel tableModel) {
		columnNames.add("Code");
		columnNames.add("Nome");
		columnNames.add("Indirizzo");
		columnNames.add("Città");
		columnNames.add("Responsabile");
		((DefaultTableModel) tableModel).setColumnIdentifiers(columnNames.toArray());
	}
	
	public JPanel getErrorZone() {
		return errorZone;
	}
	
	public JTextField getCampoUtente() {
		return campoUtente;
	}
	
	public JTextField getCampoPassword() {
		return campoPass;
	}
	
	public JTable getTableNegozi() {
		return tableNegozi;
	}
	
	public TableModel getTableModelNegozi () {
		return tableModelNegozi;
	}
	
	

	public JTextField getCampoNome() {
		return campoNome;
	}
	
	public JTextField getCampoIndirizzo() {
		return campoIndirizzo;
	}
	
	public JTextField getCampoCity() {
		return campoCity;
	}

	public JPanel getCentroCentro() {
		return centroCentro;
	}

	
	public JFrame getSegreteriaCatenaNegoziFrame() {
		return segreteriaCatenaNegoziFrame;
	}
}
