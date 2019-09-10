package InterfacciaSegreteriaCatenaNegozi;
import java.awt.*;

import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import InterfacciaResponsabileNegozio.EntrataArticoliListener;
import Login.LoginPage;
import MainClass.Ordine;
import MyList.MyList;
import MyList.MyListProxyFactory;

public class PanelMovimenti extends JPanel{
	private FrameSegreteriaCatenaNegozi frame;
	
	private final JPanel nord = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel title = new JLabel("Lista ordine negozi");
	
	private final JPanel errorZone = new JPanel();
	
	private final JPanel ovest = new JPanel(new BorderLayout());
	
	private final JPanel ovestNord = new JPanel();
	private static final JLabel mostraOrdini = new JLabel("Ordini in sospeso");
	
	private final JPanel ovestOvest = new JPanel();
	
	private final JPanel ovestCentro = new JPanel();
	private static final String[] columnNames = {"Codice Ordine", "Data Ordine", "Numero Articoli", "Prezzo Totale", "Nome Catena", "Responsabile", "Indirizzo negozio"};
	private Map<String, Ordine> mapOrder = new HashMap<>();
	private JTable tableMovimenti = new JTable();
	
	private final JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel();
	private static final JLabel articoliOrdine = new JLabel ("Articoli dell'ordine selezionato");
	
	private final JPanel centroCentro = new JPanel(new BorderLayout());
	
	private final JPanel panelTableArticoliOrdine = new JPanel();
	private static final String[] columnNamesArticleQuantity =  {"Nome", "Descrizione", "Sport", "Materiali", "Codice", "Prezzo", "Quantità", "Data Produzione"};
	private JTable tableArticoliOrdine;

	private final JPanel sud = new JPanel();
	private final JButton perOrdine = new JButton("Mostra per Ordine"); 
	private final JButton perNegozio = new JButton("Mostra per Negozio"); 
	
	public PanelMovimenti(FrameSegreteriaCatenaNegozi frame) {
		super(new BorderLayout());
		this.frame = frame;

		nord.add(new JLabel("   "));
		title.setFont(new Font("Arial", Font.BOLD, 14));
		nord.add(title);
		nord.setPreferredSize(new Dimension(0, 50));
		
		ovestNord.add(mostraOrdini);
		ovestOvest.setPreferredSize(new Dimension(50, 0));
		
		updateTableOrdini();
		
		ovest.add(ovestNord, BorderLayout.NORTH);
		ovest.add(ovestOvest, BorderLayout.WEST);
		ovest.add(ovestCentro, BorderLayout.CENTER);
		
		centroNord.add(articoliOrdine);
		centroNord.setPreferredSize(new Dimension(0, 35));
		
		updateTableArticoliOrdine(new Object[0][8]);
		centroCentro.add(panelTableArticoliOrdine);
		
		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		
		sud.add(perNegozio);
		sud.add(perOrdine);
		
		nord.setBackground(Color.WHITE);
		ovest.setBackground(Color.WHITE);
		centroNord.setBackground(Color.WHITE);
		centroCentro.setBackground(Color.WHITE);
		panelTableArticoliOrdine.setBackground(Color.WHITE);
		ovestNord.setBackground(Color.WHITE);
		ovestCentro.setBackground(Color.WHITE);
		ovestOvest.setBackground(Color.WHITE);
		errorZone.setBackground(Color.WHITE);
		sud.setBackground(Color.WHITE);
		
	
		this.add(nord, BorderLayout.NORTH);
		this.add(ovest, BorderLayout.WEST);
		this.add(centro, BorderLayout.CENTER);
		this.add(sud, BorderLayout.SOUTH);

		
		perOrdine.addActionListener(new MovimentiListener(this, frame));
		perNegozio.addActionListener(new MovimentiListener(this, frame));
	}
	
	public void updateTableOrdini() {
		
		for(Object o: frame.getProxyListOrdini().getArrayList()) {
			if( o instanceof Ordine) {
				mapOrder.put(((Ordine) o).getCodiceOrdine(), (Ordine) o);
			}
		}
		
		frame.getProxyListOrdini().displayForOrder(tableMovimenti, ovestCentro, columnNames);
		tableMovimenti.setPreferredScrollableViewportSize(new Dimension(600, 340));
		tableMovimenti.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableMovimenti.getColumnModel().getColumn(1).setPreferredWidth(100);
		tableMovimenti.getColumnModel().getColumn(2).setPreferredWidth(100);
		tableMovimenti.getColumnModel().getColumn(3).setPreferredWidth(100);
		tableMovimenti.getColumnModel().getColumn(4).setPreferredWidth(100);
		tableMovimenti.getColumnModel().getColumn(5).setPreferredWidth(100);
		tableMovimenti.getColumnModel().getColumn(6).setPreferredWidth(100);
		tableMovimenti.getSelectionModel().addListSelectionListener(new MovimentiListener(this, frame));
	}
	
	public void updateTableArticoliOrdine(Object[][] matrixArticleQuantity) {
		tableArticoliOrdine = LoginPage.getProxyListArticoli().display(panelTableArticoliOrdine, columnNamesArticleQuantity, matrixArticleQuantity);
		tableArticoliOrdine.setPreferredScrollableViewportSize(new Dimension(500, 340));
		tableArticoliOrdine.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableArticoliOrdine.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableArticoliOrdine.getColumnModel().getColumn(2).setPreferredWidth(70);
		tableArticoliOrdine.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableArticoliOrdine.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(5).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(6).setPreferredWidth(70);
		tableArticoliOrdine.getColumnModel().getColumn(7).setPreferredWidth(120);
	}

	public JPanel getErrorZone() {
		return errorZone;
	}
	
	public JTable getTableMovimenti() {
		return tableMovimenti;
	}
	
	public FrameSegreteriaCatenaNegozi getSegreteriaCatenaNegoziFrame() {
		return frame;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public Map<String, Ordine> getMapOrder() {
		return mapOrder;
	}

	public JPanel getCentroOrdini() {
		return ovestCentro;
	}
	

}
