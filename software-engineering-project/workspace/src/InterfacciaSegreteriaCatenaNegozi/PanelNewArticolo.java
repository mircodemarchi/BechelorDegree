package InterfacciaSegreteriaCatenaNegozi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import InterfacciaResponsabileNegozio.FrameResponsabileNegozio;
import MyList.MyList;
import MyList.MyListProxyFactory;

public class PanelNewArticolo extends JPanel{
	
	private FrameSegreteriaCatenaNegozi frame;
	
	private final JPanel nord = new JPanel();
	
	private final JPanel ovest = new JPanel();
	
	private JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel();
	private static final JLabel mostraArticoli = new JLabel("Articoli in DataBase:");
	
	private final JPanel centroCentro = new JPanel ();
	
	
	private ArrayList<String> columnNames = new ArrayList<String>();
	private JTable table = new JTable();
	private TableModel tableModel = new DefaultTableModel() {

	    @Override
	    public boolean isCellEditable(int row, int column) {
	       
	       return false;
	    }
	};
	
	private final JPanel centroSud = new JPanel();
	private final JButton remove =new JButton("Rimuovi Articolo"); 
	
	private final JPanel est = new JPanel(new BorderLayout());
	
	private final JPanel estNord = new JPanel(new GridLayout(2,1));

	private final JPanel errorZone = new JPanel();
	
	private final JPanel estOvest = new JPanel (new GridLayout(14,1));
	private static final JLabel testoNome = new JLabel("Articolo:"); 
	private final JPanel panelCampoNome = new JPanel();
	private static final JTextField campoNome = new JTextField(25);
	private static final JLabel testoDescrizione = new JLabel("Descrizione:"); 
	private final JPanel panelCampoDescrizione = new JPanel();
	private static final JTextField campoDescrizione = new JTextField(25);
	private static final JLabel testoSport = new JLabel("Sport:"); 
	private final JPanel panelCampoSport = new JPanel();
	private static final JTextField campoSport = new JTextField(25);
	private static final JLabel testoMateriali = new JLabel("Materiali:"); 
	private final JPanel panelCampoMateriali = new JPanel();
	private static final JTextField campoMateriali = new JTextField(15);
	private final JButton plus = new JButton("+");
	private JList<String> listaMateriali;
	private ArrayList<String> arrayListMateriali = new ArrayList<String>();
	
	private static final JLabel testoPrezzo = new JLabel("Prezzo:"); 
	private final JPanel panelCampoPrezzo = new JPanel();
	private static final JTextField campoPrezzo = new JTextField(10);
	private static final JLabel testoEuro = new JLabel("€"); 
	
	private static final JLabel testoDataProduzione = new JLabel("Data Produzione:");
	private final JPanel panelCampoDataProduzione = new JPanel();
	private static final JDateChooser calendar = new JDateChooser();
	
	private final JPanel estEst = new JPanel ();
	
	private final JPanel estSud = new JPanel();

	private final JButton add = new JButton("Aggiungi Articolo"); 
	
	private final JPanel sud = new JPanel();

	
	public PanelNewArticolo(FrameSegreteriaCatenaNegozi frame) {
		super(new BorderLayout());
		this.frame = frame;
		
		fillColumnsModel(columnNames, tableModel);
		

		
		mostraArticoli.setHorizontalAlignment(JLabel.LEFT);
		mostraArticoli.setVerticalAlignment(JLabel.CENTER);
		centroNord.add(mostraArticoli);
		
		updateTableArticoli();
		
		centroSud.add(remove);

		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		centro.add(centroSud, BorderLayout.SOUTH);
		

		errorZone.setPreferredSize(new Dimension(150, 30));
		estNord.add(errorZone);
		estNord.setPreferredSize(new Dimension(150, 40));
		
		panelCampoNome.add(campoNome);
		panelCampoDescrizione.add(campoDescrizione);
		panelCampoSport.add(campoSport);
		
		
		campoMateriali.setName("campoMateriali");
		panelCampoMateriali.add(campoMateriali);
		plus.setPreferredSize(new Dimension(20, 20));
		panelCampoMateriali.add(plus);
		String[] arrayStringMateriali = new String[arrayListMateriali.size()];
		arrayListMateriali.toArray(arrayStringMateriali);
		listaMateriali = new JList<String>(arrayStringMateriali);
		listaMateriali.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listaMateriali.setVisibleRowCount(4);
		JScrollPane scrollPane = new JScrollPane(listaMateriali, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(90, 30));
		panelCampoMateriali.add(scrollPane);
		
		
		panelCampoPrezzo.add(campoPrezzo);
		panelCampoPrezzo.add(testoEuro);
		
		calendar.getJCalendar().setPreferredSize(new Dimension(300, 100));
		calendar.getJCalendar().setWeekOfYearVisible(false);
		calendar.setBackground(Color.WHITE);
		calendar.setPreferredSize(new Dimension(250, 30));
		calendar.getDateEditor().setEnabled(false);
		panelCampoDataProduzione.add(calendar);
		
		
		estOvest.add(testoNome);
		estOvest.add(panelCampoNome);
		estOvest.add(testoDescrizione);
		estOvest.add(panelCampoDescrizione);
		estOvest.add(testoSport);
		estOvest.add(panelCampoSport);
		estOvest.add(testoMateriali);
		estOvest.add(panelCampoMateriali);
		estOvest.add(testoPrezzo);
		estOvest.add(panelCampoPrezzo);
		estOvest.add(testoDataProduzione);
		estOvest.add(panelCampoDataProduzione);

		estOvest.setPreferredSize(new Dimension(450, 700));
			
		estSud.add(add);
		
		est.add(estNord, BorderLayout.NORTH);
		est.add(estOvest, BorderLayout.WEST);
		est.add(estSud, BorderLayout.SOUTH);
		est.add(estEst, BorderLayout.EAST);
		
		nord.setBackground(Color.WHITE);
		ovest.setBackground(Color.WHITE);
		centroNord.setBackground(Color.WHITE);
		centroCentro.setBackground(Color.WHITE);
		centroSud.setBackground(Color.WHITE);
		estNord.setBackground(Color.WHITE);
		errorZone.setBackground(Color.WHITE);
		estOvest.setBackground(Color.WHITE);
		panelCampoNome.setBackground(Color.WHITE);
		panelCampoDescrizione.setBackground(Color.WHITE);
		panelCampoSport.setBackground(Color.WHITE);
		panelCampoMateriali.setBackground(Color.WHITE);


		panelCampoPrezzo.setBackground(Color.WHITE);
		panelCampoDataProduzione.setBackground(Color.WHITE);
		estEst.setBackground(Color.WHITE);
		estSud.setBackground(Color.WHITE);
		sud.setBackground(Color.WHITE);
		
		this.add(nord, BorderLayout.NORTH);
		this.add(est, BorderLayout.EAST);
		this.add(centro, BorderLayout.CENTER);
		this.add(ovest, BorderLayout.WEST);
		this.add(sud, BorderLayout.SOUTH);
		
		add.addActionListener(new NewArticoloListener(this, frame));
		remove.addActionListener(new NewArticoloListener(this, frame));
		plus.addActionListener(new NewArticoloListener(this, frame));
		campoNome.addKeyListener(new NewArticoloListener(this, frame));
		campoDescrizione.addKeyListener(new NewArticoloListener(this, frame));
		campoSport.addKeyListener(new NewArticoloListener(this, frame));
		campoMateriali.addKeyListener(new NewArticoloListener(this, frame));
		campoPrezzo.addKeyListener(new NewArticoloListener(this, frame));
		listaMateriali.addKeyListener(new NewArticoloListener(this, frame));
		
	}
	
	private void fillColumnsModel (ArrayList<String> columnNames, TableModel tableModel) {
		columnNames.add("Nome");
		columnNames.add("Descrizione");
		columnNames.add("Sport");
		columnNames.add("Materiali");
		columnNames.add("Codice");
		columnNames.add("Prezzo");
		columnNames.add("Data");
		((DefaultTableModel) tableModel).setColumnIdentifiers(columnNames.toArray());
	}
	
	public void updateTableArticoli() {
		frame.getProxyListArticoli().displayArticoli(tableModel, table, centroCentro);
	}
	
	public JPanel getErrorZone() {
		return errorZone;
	}
	
	public JTable getTable() {
		return table;
	}
	
	public TableModel getTableModel () {
		return tableModel;
	}

	public JTextField getCampoNome() {
		return campoNome;
	}
	
	public JTextField getCampoDescrizione() {
		return campoDescrizione;
	}
	
	public JTextField getCampoSport() {
		return campoSport;
	}
	
	public JTextField getCampoMateriali() {
		return campoMateriali;
	}
	
	public JDateChooser getDate() {
		return calendar;
	}
	
	public JTextField getCampoPrezzo() {
		return campoPrezzo;
	}
	
	/*public JTextField getCampoMese() {
		return campoMese;
	}
	
	public JTextField getCampoAnno() {
		return campoAnno;
	}*/

	public JPanel getCentroCentro() {
		return centroCentro;
	}

	public ArrayList<String> getArrayListMateriali() {
		return arrayListMateriali;
	}
	
	public JList<String> getListaMateriali(){
		return listaMateriali;
	}
	
	public ArrayList<String> getColumnNames () {
		return columnNames;
	}
	
	public JPanel getArticoliPanel() {
		return this;
	}
	
	public JFrame getSegreteria() {
		return frame;
	}
}
