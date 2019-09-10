package InterfacciaResponsabileNegozio;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import MainClass.Negozio;
import MainClass.Ordine;
import MyList.MyList;
import MyList.MyListProxyFactory;
import MyList.MyTableModel;

public class PanelListaOrdini extends JPanel{
	private FrameResponsabileNegozio frame;
	private Negozio negozio;
	
	private final JPanel nord = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel visualizzaOrdini = new JLabel ("Mostra gli ordini per intervallo di date o per numero ordine");
	
	private final JPanel ovest = new JPanel();
	
	private final JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel();
	private static final JLabel mostraOrdini = new JLabel("Ordini in DataBase:");
	
	private final JPanel centroCentro = new JPanel();
	private static final String[] columnNames = {"Codice Ordine", "Data Ordine", "Numero Articoli", "Prezzo Totale", "Nome Catena", "Responsabile", "Indirizzo negozio"};
	private Map<String, Ordine> mapOrder = new HashMap<>();
	private JTable table;
	
	private final JPanel centroSud = new JPanel(new BorderLayout());
	private final JPanel panelArticoliOrdine = new JPanel();
	private static final JLabel articoliOrdine = new JLabel ("Articoli dell'ordine selezionato");
	
	private final JPanel panelListaArticoli = new JPanel();
	private static final MyList proxyListArticolo = new MyListProxyFactory("ArticleDataBase");
	private static final String[] columnNamesArticleQuantity = {"Nome", "Descrizione", "Sport", "Materiali", "Codice", "Prezzo", "Quantità", "Data Produzione"};
	private JTable tableArticoliOrdine;
	
	private final JPanel est = new JPanel(new BorderLayout());
	
	private final JPanel estNord = new JPanel();
	private final JButton ricaricaOrdini = new JButton("Ripristina tutti gli ordini"); 
	
	private final JPanel estCentro = new JPanel(new BorderLayout());
	
	private final JPanel panelRicercaOrdine = new JPanel(new BorderLayout());
	private final JPanel panelRicercaPerNumeroOrdine = new JPanel(new GridLayout(2,1));
	private final JPanel panelTestoOrdine = new JPanel();
	private static final JLabel ricercaPerNumeroOrdine = new JLabel ("Ricerca per numero ordine");
	private final JPanel errorZone1 = new JPanel();
	private final JPanel panelInserisciNumeroOrdine = new JPanel();
	private final JPanel panelTestoNumeroOrdine = new JPanel();
	private static final JLabel testoNumeroOrdine = new JLabel("Numero Ordine:");
	private final JPanel panelCampoNumero = new JPanel();
	private static final JFormattedTextField campoNumero = new JFormattedTextField();
	private final JPanel panelConfirmRicerca = new JPanel();
	private final JButton confirmRicerca = new JButton("Ricerca Ordine"); 
	
	private final JPanel panelFiltraData = new JPanel(new BorderLayout());
	
	private final JPanel panelSpace = new JPanel();
	
	private final JPanel panelDate = new JPanel(new BorderLayout());
	private final JPanel panelRicercaPerDate = new JPanel(new GridLayout(2,1));
	private final JPanel panelTestoDate = new JPanel();
	private static final JLabel ricercaPerDate = new JLabel ("Ricerca per date");
	private final JPanel errorZone2 = new JPanel();
	private final JPanel panelDataInizio = new JPanel(new BorderLayout());
	private final JPanel panelTestoDataInizio = new JPanel();
	private static final JLabel testoDataInizio = new JLabel("Data inizio:"); 
	private final JPanel panelCampiDataInizio = new JPanel();
	private static final JDateChooser calendarInizio = new JDateChooser();
	//private static final JFormattedTextField campoDataInizioGiorno = new JFormattedTextField(new SimpleDateFormat("dd"));
	//private static final JFormattedTextField campoDataInizioMese = new JFormattedTextField(new SimpleDateFormat("MM"));
	//private static final JFormattedTextField campoDataInizioAnno = new JFormattedTextField(new SimpleDateFormat("YY"));
	private final JPanel panelDataFine = new JPanel(new BorderLayout());
	private final JPanel panelTestoDataFine = new JPanel();
	private static final JLabel testoDataFine = new JLabel("Data fine:");
	private final JPanel panelCampiDataFine = new JPanel();
	private static final JDateChooser calendarFine = new JDateChooser();
	//private static final JFormattedTextField campoDataFineGiorno = new JFormattedTextField(new SimpleDateFormat("dd"));
	//private static final JFormattedTextField campoDataFineMese = new JFormattedTextField(new SimpleDateFormat("MM"));
	//private static final JFormattedTextField campoDataFineAnno = new JFormattedTextField(new SimpleDateFormat("YY"));
	private final JPanel panelConfirmFiltro = new JPanel();
	private final JButton confirmFiltro = new JButton("Filtra per date"); 
	
	private final JPanel estEst = new JPanel();
	
	public PanelListaOrdini(Negozio negozio, FrameResponsabileNegozio frame) {
		super(new BorderLayout());
		this.frame = frame;
		this.negozio = negozio;
		
		nord.add(new JLabel("   "));
		visualizzaOrdini.setFont(new Font("Arial", Font.BOLD, 14));
		nord.add(visualizzaOrdini);
		nord.setPreferredSize(new Dimension(0, 50));
		
		centroNord.add(mostraOrdini);
		
		updateTableOrdini();
		
		centroSud.add(panelArticoliOrdine, BorderLayout.NORTH);
		centroSud.add(panelListaArticoli, BorderLayout.CENTER);
		
		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		centro.add(centroSud, BorderLayout.SOUTH);
		
		panelTestoOrdine.add(ricercaPerNumeroOrdine);
		errorZone1.setPreferredSize(new Dimension(150, 30));
		panelRicercaPerNumeroOrdine.add(errorZone1);
		panelRicercaPerNumeroOrdine.add(panelTestoOrdine);
		
		ricaricaOrdini.setPreferredSize(new Dimension(450, 40));
		estNord.add(ricaricaOrdini);
		estNord.setPreferredSize(new Dimension(450, 70));
		
		campoNumero.setColumns(5);
		campoNumero.setValue(0);
		testoNumeroOrdine.setPreferredSize(new Dimension(185, 20));
		panelCampoNumero.add(campoNumero);
		panelTestoNumeroOrdine.add(testoNumeroOrdine);
		panelInserisciNumeroOrdine.add(panelTestoNumeroOrdine);
		panelInserisciNumeroOrdine.add(panelCampoNumero);
		
		confirmRicerca.setPreferredSize(new Dimension(450, 40));
		panelConfirmRicerca.add(confirmRicerca);
		
		panelRicercaOrdine.add(panelRicercaPerNumeroOrdine, BorderLayout.NORTH);
		panelRicercaOrdine.add(panelInserisciNumeroOrdine, BorderLayout.CENTER);
		panelRicercaOrdine.add(panelConfirmRicerca, BorderLayout.SOUTH);
		panelRicercaOrdine.setBorder(BorderFactory.createLineBorder(new Color(242, 242, 242), 1));
		
		panelTestoDate.add(ricercaPerDate);
		errorZone2.setPreferredSize(new Dimension(150, 30));
		panelRicercaPerDate.add(errorZone2);
		panelRicercaPerDate.add(panelTestoDate);
		
		calendarInizio.setName("CalendarInizio");
		calendarInizio.setCalendar(new GregorianCalendar());
		calendarInizio.getJCalendar().setPreferredSize(new Dimension(300, 150));
		calendarInizio.getJCalendar().setWeekOfYearVisible(false);
		calendarInizio.setBackground(Color.WHITE);
		calendarInizio.setPreferredSize(new Dimension(250, 50));
		calendarFine.setName("CalendarFine");
		calendarFine.setCalendar(new GregorianCalendar());
		calendarFine.getJCalendar().setPreferredSize(new Dimension(300, 150));
		calendarFine.getJCalendar().setWeekOfYearVisible(false);
		calendarFine.setBackground(Color.WHITE);
		calendarFine.setPreferredSize(new Dimension(250, 50));
		panelCampiDataInizio.add(calendarInizio);
		panelCampiDataFine.add(calendarFine);
		
		//testoDataInizio.setPreferredSize(new Dimension(200, 20));
		//testoDataFine.setPreferredSize(new Dimension(200, 20));
		panelTestoDataInizio.add(testoDataInizio);
		panelTestoDataFine.add(testoDataFine);
		
		panelDataInizio.add(panelTestoDataInizio, BorderLayout.NORTH);
		panelDataInizio.add(panelCampiDataInizio, BorderLayout.CENTER);
		panelDataFine.add(panelTestoDataFine, BorderLayout.NORTH);
		panelDataFine.add(panelCampiDataFine, BorderLayout.CENTER);
		
		confirmFiltro.setPreferredSize(new Dimension(450, 40));
		panelConfirmFiltro.add(confirmFiltro);
		
		//panelDataInizio.setPreferredSize(new Dimension(450, 40));
		//panelDataFine.setPreferredSize(new Dimension(450, 40));
		panelDate.add(panelRicercaPerDate, BorderLayout.NORTH);
		panelDate.add(panelDataInizio, BorderLayout.WEST);
		panelDate.add(panelDataFine, BorderLayout.CENTER);
		panelDate.add(panelConfirmFiltro, BorderLayout.SOUTH);
		panelDate.setBorder(BorderFactory.createLineBorder(new Color(242, 242, 242), 1));
		
		panelSpace.setPreferredSize(new Dimension(0, 20));
		panelFiltraData.add(panelSpace, BorderLayout.NORTH);
		panelFiltraData.add(panelDate, BorderLayout.CENTER);
		
		estCentro.add(panelRicercaOrdine, BorderLayout.NORTH);
		estCentro.add(panelFiltraData, BorderLayout.CENTER);
		
		updateTableArticoliOrdine(new Object[0][8]);
		
		panelArticoliOrdine.add(articoliOrdine);
		
		est.add(estNord, BorderLayout.NORTH);
		est.add(estCentro, BorderLayout.CENTER);
		est.add(estEst, BorderLayout.EAST);
		
		nord.setBackground(Color.WHITE);
		ovest.setBackground(Color.WHITE);
		centroNord.setBackground(Color.WHITE);
		centroCentro.setBackground(Color.WHITE);
		centroSud.setBackground(Color.WHITE);
		estNord.setBackground(Color.WHITE);
		estCentro.setBackground(Color.WHITE);
		estEst.setBackground(Color.WHITE);
		est.setBackground(Color.WHITE);
		panelTestoOrdine.setBackground(Color.WHITE);
		panelTestoDate.setBackground(Color.WHITE);
		errorZone1.setBackground(Color.WHITE);
		errorZone2.setBackground(Color.WHITE);
		panelListaArticoli.setBackground(Color.WHITE);
		panelRicercaPerNumeroOrdine.setBackground(Color.WHITE);
		panelInserisciNumeroOrdine.setBackground(Color.WHITE);
		panelCampoNumero.setBackground(Color.WHITE);
		panelTestoNumeroOrdine.setBackground(Color.WHITE);
		panelConfirmRicerca.setBackground(Color.WHITE);
		panelCampiDataInizio.setBackground(Color.WHITE);
		panelCampiDataFine.setBackground(Color.WHITE);
		panelTestoDataInizio.setBackground(Color.WHITE);
		panelTestoDataFine.setBackground(Color.WHITE);
		panelSpace.setBackground(Color.WHITE);
		panelRicercaPerDate.setBackground(Color.WHITE);
		panelConfirmFiltro.setBackground(Color.WHITE);
		panelDataInizio.setBackground(Color.WHITE);
		panelDataFine.setBackground(Color.WHITE);
		panelArticoliOrdine.setBackground(Color.WHITE);
		
		this.add(nord, BorderLayout.NORTH);
		this.add(est, BorderLayout.EAST);
		this.add(centro, BorderLayout.CENTER);
		this.add(ovest, BorderLayout.WEST);
		
		ricaricaOrdini.addActionListener(new ListaOrdiniListener(this));
		confirmRicerca.addActionListener(new ListaOrdiniListener(this));
		confirmFiltro.addActionListener(new ListaOrdiniListener(this));
		//calendarInizio.addPropertyChangeListener(new ListaOrdiniListener(this));
		//calendarFine.addPropertyChangeListener(new ListaOrdiniListener(this));
	}
	
	public JTable getTable() {
		return table;
	}

	public void updateTableOrdini() {
		for(Object o: frame.getProxyListOrdine().getArrayList()) {
			if( o instanceof Ordine && ((Ordine) o).getNegozio().equals(negozio)) {
				mapOrder.put(((Ordine) o).getCodiceOrdine(), (Ordine) o);
			}
		}
		
		table = frame.getProxyListOrdine().display(centroCentro, columnNames, negozio);
		table.setPreferredScrollableViewportSize(new Dimension(620, 250));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getSelectionModel().addListSelectionListener(new ListaOrdiniListener(this));
	}

	public void updateTableArticoliOrdine(Object[][] matrixArticleQuantity) {
		tableArticoliOrdine = proxyListArticolo.display(panelListaArticoli, columnNamesArticleQuantity, matrixArticleQuantity);
		tableArticoliOrdine.setPreferredScrollableViewportSize(new Dimension(620, 140));
		tableArticoliOrdine.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableArticoliOrdine.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableArticoliOrdine.getColumnModel().getColumn(2).setPreferredWidth(70);
		tableArticoliOrdine.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableArticoliOrdine.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(5).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(6).setPreferredWidth(70);
		tableArticoliOrdine.getColumnModel().getColumn(7).setPreferredWidth(120);
	}
	
	public int updateTableOrdini(int numeroOrdine) {
		JScrollPane scrollPane;
		int numeroElementi = 0;
		
		DefaultTableModel model = new DefaultTableModel() {
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		((DefaultTableModel) model).setColumnIdentifiers(columnNames);
		
		for(Object o: frame.getProxyListOrdine().getArrayList()) {
			if( o instanceof Ordine && ((Ordine) o).getNegozio().equals(negozio) && ((Ordine) o).getCodiceOrdine().equals(Integer.toString(numeroOrdine))) {
				((DefaultTableModel) model).addRow(((Ordine) o).getLongArrayString());
				numeroElementi++;
			}
		}
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		centroCentro.removeAll();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		centroCentro.add(scrollPane);
		table.setPreferredScrollableViewportSize(new Dimension(620, 250));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.setBackground(new Color(250, 251, 232));
		table.getSelectionModel().addListSelectionListener(new ListaOrdiniListener(this));
		
		return numeroElementi;
	}
	
	
	public void updateTableOrdini(Calendar dataInizio, Calendar dataFine) {
		JScrollPane scrollPane;
		
		DefaultTableModel model = new DefaultTableModel() {
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		((DefaultTableModel) model).setColumnIdentifiers(columnNames);
		
		for(Object o: frame.getProxyListOrdine().getArrayList()) {
			if( o instanceof Ordine && ((Ordine) o).getNegozio().equals(negozio)) {
				Ordine order = ((Ordine) o);
				if((order.getData().after(dataInizio) || 
						(order.getData().get(Calendar.DAY_OF_MONTH) == dataInizio.get(Calendar.DAY_OF_MONTH) &&
						order.getData().get(Calendar.MONTH) == dataInizio.get(Calendar.MONTH)) &&
						order.getData().get(Calendar.YEAR) == dataInizio.get(Calendar.YEAR)) &&
				   (order.getData().before(dataFine) || 
						(order.getData().get(Calendar.DAY_OF_MONTH) == dataFine.get(Calendar.DAY_OF_MONTH) &&
						order.getData().get(Calendar.MONTH) == dataFine.get(Calendar.MONTH) &&
						order.getData().get(Calendar.YEAR) == dataFine.get(Calendar.YEAR)))) {
					((DefaultTableModel) model).addRow(((Ordine) o).getLongArrayString());
				}
			}
		}
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		centroCentro.removeAll();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		centroCentro.add(scrollPane);
		table.setPreferredScrollableViewportSize(new Dimension(620, 250));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.setBackground(new Color(250, 251, 232));
		table.getSelectionModel().addListSelectionListener(new ListaOrdiniListener(this));
	}
	
	public JPanel getErrorZone1() {
		return errorZone1;
	}
	
	public JPanel getErrorZone2() {
		return errorZone2;
	}

	public Map<String, Ordine> getMapOrder() {
		return mapOrder;
	}

	public FrameResponsabileNegozio getFrame() {
		return frame;
	}

	public JPanel getPanelListaArticoli() {
		return panelListaArticoli;
	}

	public String[] getColumnNamesArticleQuantity() {
		return columnNamesArticleQuantity;
	}

	public MyList getProxyListArticolo() {
		return proxyListArticolo;
	}
	
	public JDateChooser getCalendarInizio() {
		return calendarInizio;
	}
	
	public JDateChooser getCalendarFine() {
		return calendarFine;
	}
	
	public JFormattedTextField getCampoNumero() {
		return campoNumero;
	}

}
